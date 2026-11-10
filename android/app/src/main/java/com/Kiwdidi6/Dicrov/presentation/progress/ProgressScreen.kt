package com.Kiwdidi6.Dicrov.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldDim
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Success
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.GlassCard
import com.Kiwdidi6.Dicrov.core.ui.ScreenScaffold
import com.Kiwdidi6.Dicrov.core.ui.StatItem
import com.Kiwdidi6.Dicrov.core.ui.StatRow
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile
import com.Kiwdidi6.Dicrov.core.ui.formatAmount
import com.Kiwdidi6.Dicrov.core.ui.formatMultiplier
import com.Kiwdidi6.Dicrov.core.ui.formatRatio
import com.Kiwdidi6.Dicrov.core.ui.nameRes
import com.Kiwdidi6.Dicrov.core.ui.perkRes

@Composable
fun ProgressScreen(onMenu: () -> Unit) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(titleRes = R.string.club_progress, onMenu = onMenu) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.rank_track),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.ranks.forEach { node ->
                            RankNode(node = node)
                        }
                    }
                    Text(
                        text = stringResource(state.currentRank.perkRes()),
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldLight
                    )
                }
            }

            StatRow(
                items = listOf(
                    StatItem(
                        R.string.stat_rooms_cleared,
                        state.totalRooms,
                        Success,
                        formatRatio(state.roomsCleared, state.totalRooms)
                    ),
                    StatItem(
                        R.string.stat_stars,
                        state.maxStars,
                        GoldLight,
                        formatRatio(state.stars, state.maxStars)
                    )
                )
            )
            StatRow(
                items = listOf(
                    StatItem(
                        R.string.stat_total_spins,
                        state.totalSpins,
                        Gold,
                        formatAmount(state.totalSpins)
                    ),
                    StatItem(
                        R.string.stat_best_win,
                        state.bestWin,
                        GoldLight,
                        formatAmount(state.bestWin)
                    ),
                    StatItem(
                        R.string.stat_best_room,
                        state.bestRoomScore,
                        Success,
                        formatAmount(state.bestRoomScore)
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

            Text(
                text = stringResource(R.string.club_shelf),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.decorations.forEach { decoration ->
                    DecorationSlot(decoration = decoration)
                }
            }

            Text(
                text = stringResource(R.string.room_history),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            state.history.forEach { room ->
                HistoryRow(room = room)
            }
        }
    }
}

@Composable
private fun RankNode(node: RankNodeState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (node.current) 26.dp else 20.dp)
                .clip(CircleShape)
                .background(
                    if (node.reached) {
                        Brush.linearGradient(listOf(GoldLight, Gold))
                    } else {
                        Brush.linearGradient(listOf(Color(0x1FFFFFFF), Color(0x1FFFFFFF)))
                    }
                )
                .border(
                    1.dp,
                    if (node.current) GoldLight else Color(0x1FFFFFFF),
                    CircleShape
                )
        )
        Text(
            text = stringResource(node.rank.nameRes()),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            color = if (node.reached) GoldLight else TextMuted
        )
        Text(
            text = formatAmount(node.rank.starsRequired),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun DecorationSlot(decoration: DecorationState) {
    val shape = RoundedCornerShape(14.dp)
    val description = stringResource(
        if (decoration.unlocked) R.string.state_unlocked else R.string.state_locked
    )
    Column(
        modifier = Modifier
            .width(108.dp)
            .clip(shape)
            .background(
                if (decoration.unlocked) Color(0x1AD4AF37) else Color(0x14FFFFFF)
            )
            .border(
                1.dp,
                if (decoration.unlocked) Gold.copy(alpha = 0.5f) else Color(0x1FFFFFFF),
                shape
            )
            .semantics { stateDescription = description }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SymbolTile(
            symbol = decoration.symbol,
            size = 48.dp,
            dimmed = !decoration.unlocked,
            plate = false,
            modifier = Modifier.alpha(if (decoration.unlocked) 1f else 0.4f)
        )
        Text(
            text = stringResource(decoration.nameRes),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = if (decoration.unlocked) GoldLight else TextMuted,
            textAlign = TextAlign.Center
        )
        if (!decoration.unlocked) {
            Text(
                text = stringResource(decoration.requirementRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HistoryRow(room: RoomHistoryState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x14FFFFFF))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(room.nameRes),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            color = TextSecondary
        )
        if (room.played) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(3) { index ->
                        Icon(
                            painter = painterResource(
                                if (index < room.stars) {
                                    R.drawable.ic_star
                                } else {
                                    R.drawable.ic_star_outline
                                }
                            ),
                            contentDescription = null,
                            tint = if (index < room.stars) GoldLight else GoldDim,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Text(
                    text = formatAmount(room.bestScore),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = GoldLight
                )
            }
        } else {
            Text(
                text = stringResource(R.string.not_played_yet),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted
            )
        }
    }
}
