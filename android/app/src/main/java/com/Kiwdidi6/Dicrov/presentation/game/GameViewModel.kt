package com.Kiwdidi6.Dicrov.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.RoomOutcome
import com.Kiwdidi6.Dicrov.domain.model.RoomResult
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import com.Kiwdidi6.Dicrov.domain.model.SpinOutcome
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import com.Kiwdidi6.Dicrov.domain.repository.SettingsRepository
import com.Kiwdidi6.Dicrov.domain.usecase.ApplyRoomResultUseCase
import com.Kiwdidi6.Dicrov.domain.usecase.BuildReelStripsUseCase
import com.Kiwdidi6.Dicrov.domain.usecase.SpinReelsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

val SpinEasing = CubicBezierEasing(0.05f, 0.7f, 0.15f, 1f)

class GameViewModel(
    private val club: ClubRepository,
    private val settingsRepository: SettingsRepository,
    private val buildReelStrips: BuildReelStripsUseCase,
    private val spinReels: SpinReelsUseCase,
    private val applyRoomResult: ApplyRoomResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    val reelOffsets: List<Animatable<Float, AnimationVector1D>> =
        List(GameConfig.REELS) { Animatable(0f) }

    private var spinJob: Job? = null
    private var vaultJob: Job? = null
    private var loadedAttempt = -1
    private var random = Random(0)
    private var lossStreak = 0
    private var bigWinCooldown = 0
    private var shardsGained = 0
    private var chipsWonTotal = 0
    private var bestStreak = 1f
    private var collectedAtStart: Set<SlotSymbol> = emptySet()
    private var finished = false

    fun startRoom(roomIndex: Int, attempt: Int) {
        if (loadedAttempt == attempt) return
        loadedAttempt = attempt
        spinJob?.cancel()
        vaultJob?.cancel()
        finished = false
        lossStreak = 0
        bigWinCooldown = 0
        shardsGained = 0
        chipsWonTotal = 0
        bestStreak = 1f

        val profile = club.profile.value
        val settings = settingsRepository.settings.value
        val room = SampleData.roomAt(roomIndex)
        val seed = roomIndex * GameConfig.STRIP_SEED_A xor (attempt * GameConfig.STRIP_SEED_B)
        random = Random(seed)
        collectedAtStart = SlotSymbol.entries.filter { profile.isSymbolCollected(it) }.toSet()

        val strips = buildReelStrips(room, seed)
        val stops = strips.map { random.nextInt(it.size) }
        val budget = room.spins + profile.rank.extraSpins

        viewModelScope.launch {
            reelOffsets.forEachIndexed { index, animatable ->
                animatable.snapTo(stops[index].toFloat())
            }
        }

        _state.value = GameUiState(
            roomIndex = roomIndex,
            roomNameRes = room.nameRes,
            strips = strips,
            stops = stops,
            featured = room.featured,
            status = GameStatus.IDLE,
            chips = profile.chips,
            spinBudget = budget,
            spinsLeft = budget,
            spinsUsed = 0,
            points = 0,
            target = room.targetPoints,
            lastWin = 0,
            charge = 0,
            chargeCap = profile.rank.chargeCap,
            combo = 1f,
            showHint = true,
            payLineHints = settings.payLineHints,
            reducedMotion = settings.reducedMotion
        )
    }

    fun spin() {
        val current = _state.value
        if (!current.canSpin) return

        val settings = settingsRepository.settings.value
        val speed = if (current.turbo) ReelSpeed.FAST else settings.reelSpeed
        val forceWild = current.charge >= current.chargeCap
        val pity = lossStreak >= GameConfig.PITY_LOSS_STREAK

        val outcome = spinReels(
            strips = current.strips,
            random = random,
            bet = GameConfig.BET,
            pityActive = pity,
            forceWild = forceWild,
            suppressBigWin = bigWinCooldown > 0
        )

        club.update { profile ->
            profile.copy(
                chips = profile.chips - GameConfig.BET,
                totalSpins = profile.totalSpins + 1
            )
        }

        _state.update { previous ->
            previous.copy(
                status = GameStatus.SPINNING,
                chips = previous.chips - GameConfig.BET,
                spinsLeft = previous.spinsLeft - 1,
                spinsUsed = previous.spinsUsed + 1,
                lastWin = 0,
                winningLines = emptyList(),
                winningCells = emptySet(),
                bigWin = false,
                showHint = false
            )
        }

        spinJob = viewModelScope.launch {
            val animations = reelOffsets.mapIndexed { index, animatable ->
                async {
                    val stripSize = current.strips[index].size
                    val base = floor(animatable.value).toInt()
                    val loops = 4 + index * 2
                    var target = base + loops * stripSize
                    val delta = ((outcome.stops[index] - target) % stripSize + stripSize) % stripSize
                    target += delta
                    val duration =
                        ((GameConfig.STOP_BASE_MS + index * GameConfig.STOP_STAGGER_MS) * speed.factor)
                            .toInt()
                    animatable.animateTo(
                        targetValue = target.toFloat(),
                        animationSpec = tween(durationMillis = duration, easing = SpinEasing)
                    )
                }
            }
            animations.awaitAll()
            _state.update { it.copy(status = GameStatus.EVALUATING, stops = outcome.stops) }
            delay(GameConfig.EVALUATE_HOLD_MS)
            settleOutcome(outcome, settings.celebration)
        }
    }

    private suspend fun settleOutcome(outcome: SpinOutcome, celebration: WinCelebration) {
        val profile = club.profile.value
        val before = _state.value
        val win = outcome.chipsWon
        val combo = if (win > 0) nextCombo(before.combo, profile.rank.comboCap) else 1f
        if (win > 0) lossStreak = 0 else lossStreak += 1
        bestStreak = max(bestStreak, combo)

        val gainedPoints = floor(win / 2.0 * combo).toInt()
        val charge = when {
            outcome.forcedWild -> 0
            win > 0 -> before.charge
            else -> min(before.charge + 1, before.chargeCap)
        }
        val shardDeltas = shardsFor(outcome, before.featured, profile)
        shardsGained += shardDeltas.values.sum()
        chipsWonTotal += win
        val bigWin = win >= GameConfig.BET * GameConfig.BIG_WIN_MULTIPLE
        bigWinCooldown = if (bigWin) 2 else max(0, bigWinCooldown - 1)

        club.update { stored ->
            val merged = stored.shards.toMutableMap()
            shardDeltas.forEach { (symbol, amount) ->
                merged[symbol] = (merged[symbol] ?: 0) + amount
            }
            stored.copy(
                chips = stored.chips + win,
                bestWin = max(stored.bestWin, win),
                shards = merged,
                bestStreak = max(stored.bestStreak, combo)
            )
        }

        _state.update {
            it.copy(
                status = if (win > 0) GameStatus.WINNING else GameStatus.IDLE,
                chips = it.chips + win,
                lastWin = win,
                points = it.points + gainedPoints,
                combo = combo,
                charge = charge,
                winningLines = outcome.wins.map { line -> line.line },
                winningCells = winningCellsOf(outcome),
                bigWin = bigWin
            )
        }

        if (win > 0) {
            val hold = if (bigWin) {
                if (celebration == WinCelebration.FULL) {
                    GameConfig.BIG_WIN_FULL_MS
                } else {
                    GameConfig.BIG_WIN_SHORT_MS
                }
            } else {
                GameConfig.WIN_FLASH_MS
            }
            delay(hold)
            _state.update { it.copy(status = GameStatus.IDLE) }
        }

        if (bigWin) {
            openVault()
            return
        }
        checkRoomEnd()
    }

    private fun openVault() {
        val rewards = SampleData.vaultRewards.shuffled(random)
        _state.update { it.copy(showVaultDialog = true, vaultRewards = rewards, vaultPick = -1) }
        vaultJob?.cancel()
        vaultJob = viewModelScope.launch {
            delay(VAULT_AUTO_PICK_MS)
            if (_state.value.showVaultDialog && _state.value.vaultPick < 0) {
                pickVaultDoor(random.nextInt(rewards.size))
            }
        }
    }

    fun pickVaultDoor(index: Int) {
        val current = _state.value
        if (!current.showVaultDialog || current.vaultPick >= 0) return
        vaultJob?.cancel()
        val reward = current.vaultRewards.getOrElse(index) { SampleData.vaultRewards.first() }
        val featured = current.featured
        chipsWonTotal += reward
        shardsGained += VAULT_SHARDS
        club.update { stored ->
            val merged = stored.shards.toMutableMap()
            merged[featured] = (merged[featured] ?: 0) + VAULT_SHARDS
            stored.copy(chips = stored.chips + reward, shards = merged)
        }
        _state.update { it.copy(vaultPick = index, chips = it.chips + reward) }
    }

    fun closeVault() {
        if (_state.value.vaultPick < 0) pickVaultDoor(0)
        vaultJob?.cancel()
        _state.update { it.copy(showVaultDialog = false) }
        checkRoomEnd()
    }

    fun openPause() {
        spinJob?.cancel()
        _state.update { it.copy(showPauseDialog = true, status = GameStatus.IDLE) }
    }

    fun closePause() {
        _state.update { it.copy(showPauseDialog = false) }
    }

    fun restartRoom() {
        val index = _state.value.roomIndex
        val attempt = loadedAttempt
        loadedAttempt = -1
        startRoom(index, attempt + 1_000)
    }

    fun endLevel() {
        _state.update { it.copy(showPauseDialog = false) }
        finishRoom(outcomeFor(_state.value))
    }

    fun toggleTurbo() {
        _state.update { it.copy(turbo = !it.turbo) }
    }

    fun pause() {
        spinJob?.cancel()
        vaultJob?.cancel()
    }

    private fun checkRoomEnd() {
        val current = _state.value
        when {
            current.points >= current.target -> finishRoom(RoomOutcome.CLEARED)
            current.spinsLeft <= 0 -> finishRoom(outcomeFor(current))
            current.outOfChips -> finishRoom(RoomOutcome.OUT_OF_CHIPS)
        }
    }

    private fun outcomeFor(state: GameUiState): RoomOutcome = when {
        state.points >= state.target -> RoomOutcome.CLEARED
        state.outOfChips -> RoomOutcome.OUT_OF_CHIPS
        else -> RoomOutcome.FAILED
    }

    private fun finishRoom(outcome: RoomOutcome) {
        if (finished) return
        finished = true
        spinJob?.cancel()
        vaultJob?.cancel()
        val current = _state.value
        val stars = applyRoomResult.starsFor(current.points, current.target)
        val unlockedDecoration = SampleData.decorations.firstOrNull { decoration ->
            decoration.rankRequired == null &&
                club.profile.value.isSymbolCollected(decoration.symbol) &&
                !collectedAtStart.contains(decoration.symbol)
        }
        val result = RoomResult(
            roomIndex = current.roomIndex,
            outcome = outcome,
            points = current.points,
            target = current.target,
            chipsWon = chipsWonTotal,
            shardsGained = shardsGained,
            stars = stars,
            bestStreak = bestStreak,
            spinsUsed = current.spinsUsed,
            spinBudget = current.spinBudget,
            unlockedDecorationId = unlockedDecoration?.id ?: -1
        )
        club.update { applyRoomResult(it, result, SampleData.rooms.size) }
        club.publishResult(result)
        _state.update { it.copy(status = GameStatus.FINISHED, showPauseDialog = false) }
        viewModelScope.launch {
            delay(ROOM_END_FADE_MS)
            eventChannel.send(GameEvent.RoomFinished(current.roomIndex))
        }
    }

    private fun nextCombo(current: Float, cap: Float): Float = when {
        current < 1.25f -> 1.25f
        current < 1.5f -> 1.5f
        current < 2f -> 2f
        else -> cap
    }.coerceAtMost(cap)

    private fun shardsFor(
        outcome: SpinOutcome,
        featured: SlotSymbol,
        profile: ClubProfile
    ): Map<SlotSymbol, Int> {
        val deltas = mutableMapOf<SlotSymbol, Int>()
        outcome.wins.forEach { win ->
            val base = if (win.symbol == featured) 2 else 1
            val bonus = if (profile.rank.shardBonusPercent > 0 && random.nextInt(100) < 10) 1 else 0
            deltas[win.symbol] = (deltas[win.symbol] ?: 0) + base + bonus
        }
        return deltas
    }

    private fun winningCellsOf(outcome: SpinOutcome): Set<Int> {
        val cells = mutableSetOf<Int>()
        outcome.wins.forEach { win ->
            win.line.rows.forEachIndexed { reel, row ->
                cells.add(reel * GameConfig.ROWS + row)
            }
        }
        return cells
    }

    private companion object {
        const val VAULT_AUTO_PICK_MS = 9_000L
        const val ROOM_END_FADE_MS = 360L
        const val VAULT_SHARDS = 3
    }
}
