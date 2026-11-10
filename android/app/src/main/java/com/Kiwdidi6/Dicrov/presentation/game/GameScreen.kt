package com.Kiwdidi6.Dicrov.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldAmbient
import com.Kiwdidi6.Dicrov.core.theme.GoldDim
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.GoldSpot
import com.Kiwdidi6.Dicrov.core.theme.Success
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.AppBackground
import com.Kiwdidi6.Dicrov.core.ui.ChargeMeter
import com.Kiwdidi6.Dicrov.core.ui.SecondaryButton
import com.Kiwdidi6.Dicrov.core.ui.StatItem
import com.Kiwdidi6.Dicrov.core.ui.StatRow
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile
import com.Kiwdidi6.Dicrov.core.ui.formatAmount
import com.Kiwdidi6.Dicrov.core.ui.formatMultiplier
import com.Kiwdidi6.Dicrov.core.ui.formatRatio
import com.Kiwdidi6.Dicrov.core.ui.nameRes
import com.Kiwdidi6.Dicrov.presentation.dialog.PauseDialog
import com.Kiwdidi6.Dicrov.presentation.dialog.VaultBonusDialog
import kotlin.math.floor

private val GameOverlay = listOf(
    BgBase.copy(alpha = 0.80f),
    Surface1.copy(alpha = 0.86f),
    BgBase.copy(alpha = 0.92f)
)

private val BoardPad = 10.dp
private val BoardBorder = 3.dp
private val ReelGap = 6.dp

@Composable
fun GameScreen(
    roomIndex: Int,
    attempt: Int,
    onRoomFinished: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(roomIndex, attempt) {
        viewModel.startRoom(roomIndex, attempt)
    }

    DisposableEffect(roomIndex, attempt) {
        onDispose { viewModel.pause() }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.RoomFinished -> onRoomFinished(event.roomIndex)
            }
        }
    }

    val winScale by animateFloatAsState(
        targetValue = if (state.status == GameStatus.WINNING) 1.18f else 1f,
        animationSpec = tween(160),
        label = "win_scale"
    )

    AppBackground(overlay = GameOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameHeader(
                state = state,
                onPause = { viewModel.openPause() }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FeaturedBanner(state = state)
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ReelBoard(state = state, offsets = viewModel.reelOffsets, winScale = winScale)
            }
            if (state.showHint) {
                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            GameStats(state = state)
            ChargeMeter(
                charge = state.charge,
                cap = state.chargeCap,
                reducedMotion = state.reducedMotion
            )
            Controls(
                state = state,
                onSpin = { viewModel.spin() },
                onTurbo = { viewModel.toggleTurbo() }
            )
        }
    }

    if (state.showPauseDialog) {
        PauseDialog(
            roomNameRes = state.roomNameRes,
            onResume = { viewModel.closePause() },
            onRestart = { viewModel.restartRoom() },
            onEndLevel = { viewModel.endLevel() },
            onMenu = onMenu
        )
    }

    if (state.showVaultDialog) {
        VaultBonusDialog(
            rewards = state.vaultRewards,
            pickedIndex = state.vaultPick,
            onPick = { index -> viewModel.pickVaultDoor(index) },
            onContinue = { viewModel.closeVault() }
        )
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onPause,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = GoldLight
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(state.roomNameRes),
                style = MaterialTheme.typography.titleSmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.spins_left, state.spinsLeft),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chips),
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = stringResource(R.string.chips_label),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted
            )
            Text(
                text = formatAmount(state.chips),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = GoldLight
            )
        }
    }
}

@Composable
private fun FeaturedBanner(state: GameUiState) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0x1AD4AF37))
            .border(1.dp, Gold.copy(alpha = 0.35f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        SymbolTile(symbol = state.featured, size = 22.dp, plate = false)
        Text(
            text = stringResource(
                R.string.featured_symbol,
                stringResource(state.featured.nameRes())
            ),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = GoldLight
        )
    }
}

@Composable
private fun ReelBoard(
    state: GameUiState,
    offsets: List<Animatable<Float, AnimationVector1D>>,
    winScale: Float
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val frame = BoardPad + BoardBorder
        val widthBudget = min(maxWidth, 372.dp) - frame * 2 - ReelGap * 2
        val heightBudget = maxHeight - frame * 2
        val raw = min(widthBudget / GameConfig.REELS, heightBudget / GameConfig.ROWS)
        val cell = Dp(floor(raw.value).coerceAtLeast(28f))
        val boardWidth = cell * GameConfig.REELS + ReelGap * 2 + frame * 2
        val boardHeight = cell * GameConfig.ROWS + frame * 2
        val shape = RoundedCornerShape(18.dp)
        val lineAlpha by animateFloatAsState(
            targetValue = when {
                state.winningLines.isNotEmpty() && state.status == GameStatus.WINNING -> 1f
                state.payLineHints -> 0.35f
                else -> 0f
            },
            animationSpec = tween(400),
            label = "pay_line_alpha"
        )

        Box(
            modifier = Modifier
                .width(boardWidth)
                .height(boardHeight)
                .shadow(14.dp, shape, ambientColor = GoldAmbient, spotColor = GoldSpot)
                .clip(shape)
                .background(Brush.verticalGradient(listOf(Surface1, BgBase)))
                .border(
                    BoardBorder,
                    Brush.linearGradient(listOf(Gold, GoldLight, GoldDim, Gold)),
                    shape
                )
                .padding(frame),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(ReelGap)) {
                for (reel in 0 until GameConfig.REELS) {
                    ReelColumn(
                        strip = state.strips.getOrElse(reel) { emptyList() },
                        offset = offsets[reel].value,
                        cell = cell,
                        rows = GameConfig.ROWS,
                        reelIndex = reel,
                        winningCells = state.winningCells,
                        winScale = winScale
                    )
                }
            }
            if (lineAlpha > 0.01f) {
                Canvas(modifier = Modifier.matchParentSize().alpha(lineAlpha)) {
                    val w = size.width
                    val h = size.height
                    drawLine(GoldLight, Offset(0f, h / 2f), Offset(w, h / 2f), 2f)
                    drawLine(GoldLight, Offset(0f, h / 6f), Offset(w, h * 5f / 6f), 2f)
                    drawLine(GoldLight, Offset(0f, h * 5f / 6f), Offset(w, h / 6f), 2f)
                }
            }
        }
    }
}

@Composable
private fun GameStats(state: GameUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        StatRow(
            items = listOf(
                StatItem(
                    R.string.stat_bet,
                    GameConfig.BET,
                    GoldLight,
                    formatAmount(GameConfig.BET)
                ),
                StatItem(
                    R.string.stat_points,
                    state.target,
                    Gold,
                    formatRatio(state.points, state.target)
                ),
                StatItem(
                    R.string.stat_last_win,
                    state.lastWin,
                    Success,
                    formatAmount(state.lastWin)
                )
            )
        )
        if (state.combo > 1f) {
            Text(
                text = stringResource(R.string.combo_chip, formatMultiplier(state.combo)),
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x1AD4AF37))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun Controls(
    state: GameUiState,
    onSpin: () -> Unit,
    onTurbo: () -> Unit
) {
    val spinState = when {
        state.status == GameStatus.SPINNING -> stringResource(R.string.state_spinning)
        state.outOfChips -> stringResource(R.string.state_out_of_chips)
        else -> stringResource(R.string.state_ready)
    }
    val turboState = stringResource(if (state.turbo) R.string.state_on else R.string.state_off)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(146.dp)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(74.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.stat_bet),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted
            )
            Text(
                text = formatAmount(GameConfig.BET),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = GoldLight
            )
        }

        Button(
            onClick = onSpin,
            enabled = state.canSpin,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = BgBase,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = BgBase
            ),
            modifier = Modifier
                .size(118.dp)
                .shadow(16.dp, CircleShape, ambientColor = GoldAmbient, spotColor = GoldSpot)
                .semantics { stateDescription = spinState }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(listOf(GoldLight, Gold, GoldDim)),
                        CircleShape
                    )
                    .alpha(if (state.canSpin) 1f else 0.45f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_spin),
                        contentDescription = null,
                        tint = BgBase,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = stringResource(R.string.spin),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = BgBase
                    )
                }
            }
        }

        SecondaryButton(
            textRes = R.string.turbo,
            onClick = onTurbo,
            contentColor = if (state.turbo) BgBase else GoldLight,
            borderColor = if (state.turbo) GoldLight else Gold.copy(alpha = 0.35f),
            modifier = Modifier
                .width(74.dp)
                .semantics { stateDescription = turboState }
        )
    }
}
