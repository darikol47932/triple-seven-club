package com.Kiwdidi6.Dicrov.presentation.result

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldDim
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Success
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.AppBackground
import com.Kiwdidi6.Dicrov.core.ui.GlassCard
import com.Kiwdidi6.Dicrov.core.ui.PrimaryButton
import com.Kiwdidi6.Dicrov.core.ui.SecondaryButton
import com.Kiwdidi6.Dicrov.core.ui.StatItem
import com.Kiwdidi6.Dicrov.core.ui.StatRow
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile
import com.Kiwdidi6.Dicrov.core.ui.formatAmount
import com.Kiwdidi6.Dicrov.core.ui.formatMultiplier
import com.Kiwdidi6.Dicrov.core.ui.formatRatio
import com.Kiwdidi6.Dicrov.domain.model.RoomOutcome

@Composable
fun ResultScreen(
    onPlayAgain: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: ResultViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cleared = state.outcome == RoomOutcome.CLEARED

    val overlay = if (cleared) {
        listOf(SurfaceWarm.copy(alpha = 0.88f), BgBase.copy(alpha = 0.96f))
    } else {
        listOf(Surface1.copy(alpha = 0.91f), BgBase.copy(alpha = 0.97f))
    }

    AppBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    when (state.outcome) {
                        RoomOutcome.CLEARED -> R.string.room_cleared
                        RoomOutcome.FAILED -> R.string.room_not_cleared
                        RoomOutcome.OUT_OF_CHIPS -> R.string.out_of_chips
                    }
                ),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 30.sp,
                color = if (cleared) GoldLight else TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = if (state.chipsWon > 0) {
                    stringResource(R.string.result_won, formatAmount(state.chipsWon))
                } else {
                    stringResource(R.string.result_no_luck)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (state.chipsWon > 0) Success else TextMuted,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(state.roomNameRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )

            StarReveal(stars = state.stars)

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatRow(
                        items = listOf(
                            StatItem(
                                R.string.stat_club_points,
                                state.target,
                                Gold,
                                formatRatio(state.points, state.target)
                            ),
                            StatItem(
                                R.string.stat_spins_used,
                                state.spinBudget,
                                GoldLight,
                                formatRatio(state.spinsUsed, state.spinBudget)
                            )
                        )
                    )
                    StatRow(
                        items = listOf(
                            StatItem(
                                R.string.stat_chips_won,
                                state.chipsWon,
                                Success,
                                formatAmount(state.chipsWon)
                            ),
                            StatItem(
                                R.string.stat_shards,
                                state.shardsGained,
                                GoldLight,
                                formatAmount(state.shardsGained)
                            )
                        )
                    )
                    if (state.bestStreak > 1f) {
                        Text(
                            text = stringResource(
                                R.string.best_streak,
                                formatMultiplier(state.bestStreak)
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            val decorationSymbol = state.unlockedDecorationSymbol
            if (state.unlockedDecorationNameRes != 0 && decorationSymbol != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x1AD4AF37))
                        .border(1.dp, Gold.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SymbolTile(
                        symbol = decorationSymbol,
                        size = 40.dp,
                        plate = false
                    )
                    Text(
                        text = stringResource(
                            R.string.unlocked_decoration,
                            stringResource(state.unlockedDecorationNameRes)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 13.sp,
                        color = GoldLight
                    )
                }
            }

            PrimaryButton(
                textRes = R.string.play_again,
                onClick = { onPlayAgain(viewModel.onPlayAgain()) },
                iconRes = R.drawable.ic_play,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                textRes = R.string.menu,
                onClick = onMenu,
                spriteRes = R.drawable.icon_home,
                height = 52.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.disclaimer_result),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StarReveal(stars: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(3) { index ->
            val earned = index < stars
            val scale by animateFloatAsState(
                targetValue = if (earned) 1f else 0.82f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                label = "star_reveal"
            )
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (earned) R.drawable.ic_star else R.drawable.ic_star_outline
                    ),
                    contentDescription = null,
                    tint = if (earned) GoldLight else GoldDim,
                    modifier = Modifier
                        .size(36.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }
    }
}
