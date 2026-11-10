package com.Kiwdidi6.Dicrov.presentation.game

import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.domain.model.PayLine
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

enum class GameStatus { IDLE, SPINNING, EVALUATING, WINNING, FINISHED }

data class GameUiState(
    val roomIndex: Int = 0,
    val roomNameRes: Int = R.string.room_velvet_lounge,
    val strips: List<List<SlotSymbol>> = emptyList(),
    val stops: List<Int> = List(GameConfig.REELS) { 0 },
    val featured: SlotSymbol = SlotSymbol.CHERRY,
    val status: GameStatus = GameStatus.IDLE,
    val chips: Int = GameConfig.STARTING_CHIPS,
    val spinBudget: Int = 8,
    val spinsLeft: Int = 8,
    val spinsUsed: Int = 0,
    val points: Int = 0,
    val target: Int = 120,
    val lastWin: Int = 0,
    val charge: Int = 0,
    val chargeCap: Int = GameConfig.CHARGE_CAP,
    val combo: Float = 1f,
    val winningLines: List<PayLine> = emptyList(),
    val winningCells: Set<Int> = emptySet(),
    val bigWin: Boolean = false,
    val showHint: Boolean = true,
    val showPauseDialog: Boolean = false,
    val showVaultDialog: Boolean = false,
    val vaultRewards: List<Int> = emptyList(),
    val vaultPick: Int = -1,
    val turbo: Boolean = false,
    val payLineHints: Boolean = true,
    val reducedMotion: Boolean = false
) {
    val canSpin: Boolean
        get() = status == GameStatus.IDLE &&
            !showPauseDialog &&
            !showVaultDialog &&
            spinsLeft > 0 &&
            chips >= GameConfig.BET

    val outOfChips: Boolean
        get() = chips < GameConfig.BET
}

sealed interface GameEvent {
    data class RoomFinished(val roomIndex: Int) : GameEvent
}
