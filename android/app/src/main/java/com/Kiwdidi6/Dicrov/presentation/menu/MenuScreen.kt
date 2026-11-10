package com.Kiwdidi6.Dicrov.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldAmbient
import com.Kiwdidi6.Dicrov.core.theme.GoldDim
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.GoldSpot
import com.Kiwdidi6.Dicrov.core.theme.Success
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.AppBackground
import com.Kiwdidi6.Dicrov.core.ui.PrimaryButton
import com.Kiwdidi6.Dicrov.core.ui.RouteTile
import com.Kiwdidi6.Dicrov.core.ui.StatItem
import com.Kiwdidi6.Dicrov.core.ui.StatRow
import com.Kiwdidi6.Dicrov.core.ui.formatAmount
import com.Kiwdidi6.Dicrov.core.ui.nameRes

private val MenuOverlay = listOf(
    BgBase.copy(alpha = 0.58f),
    Surface1.copy(alpha = 0.74f),
    BgBase.copy(alpha = 0.90f)
)

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onLevelSelect: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    val transition = rememberInfiniteTransition(label = "menu")
    val bloom by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.24f,
        animationSpec = infiniteRepeatable(tween(2600), repeatMode = RepeatMode.Reverse),
        label = "menu_bloom"
    )

    AppBackground(
        overlay = MenuOverlay,
        bloom = Brush.radialGradient(
            colors = listOf(Gold.copy(alpha = bloom), Color.Transparent),
            radius = 760f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MenuHeader(chips = state.chips)
            RankStrip(state = state)
            PlayTile(state = state, onPlay = { onPlay(state.nextRoomIndex) })
            RouteGrid(
                state = state,
                onLevelSelect = onLevelSelect,
                onCollection = onCollection,
                onProgress = onProgress,
                onSettings = onSettings
            )
            MenuStats(state = state)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.disclaimer_menu),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MenuHeader(chips: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_club_emblem),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                color = GoldLight
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(SurfaceWarm.copy(alpha = 0.7f))
                .border(1.dp, Gold.copy(alpha = 0.35f), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 7.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chips),
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = stringResource(R.string.club_balance),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextMuted
            )
            Text(
                text = formatAmount(chips),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = GoldLight
            )
        }
    }
}

@Composable
private fun RankStrip(state: MenuUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x14FFFFFF))
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.club_rank_value, stringResource(state.rank.nameRes())),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = GoldLight,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = stringResource(R.string.stars_of, state.stars, state.maxStars),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
        ) {
            val fraction = if (state.maxStars > 0) {
                (state.stars.toFloat() / state.maxStars.toFloat()).coerceIn(0.02f, 1f)
            } else {
                0.02f
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(GoldDim, GoldLight)))
            )
        }
    }
}

@Composable
private fun PlayTile(state: MenuUiState, onPlay: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, shape, ambientColor = GoldAmbient, spotColor = GoldSpot)
            .clip(shape)
            .background(Brush.linearGradient(listOf(SurfaceWarm, Color(0xFF2A2317), Surface1)))
            .border(
                2.dp,
                Brush.linearGradient(listOf(Gold, GoldLight, GoldDim)),
                shape
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(58.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = stringResource(R.string.menu_next_room),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextMuted
                )
                Text(
                    text = stringResource(state.nextRoomNameRes),
                    style = MaterialTheme.typography.headlineSmall,
                    color = GoldLight
                )
                Text(
                    text = stringResource(
                        R.string.menu_room_goal,
                        state.nextRoomTarget,
                        state.nextRoomSpins
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
        PrimaryButton(
            textRes = R.string.play,
            onClick = onPlay,
            iconRes = R.drawable.ic_play,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RouteGrid(
    state: MenuUiState,
    onLevelSelect: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RouteTile(
                labelRes = R.string.level_select,
                subline = stringResource(
                    R.string.menu_rooms_subline,
                    state.totalRooms,
                    state.unlockedRooms
                ),
                iconRes = R.drawable.ic_rooms,
                onClick = onLevelSelect,
                modifier = Modifier.weight(1f)
            )
            RouteTile(
                labelRes = R.string.collection,
                subline = stringResource(
                    R.string.menu_collection_subline,
                    state.collectedSymbols,
                    state.totalSymbols
                ),
                iconRes = R.drawable.ic_diamond,
                onClick = onCollection,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RouteTile(
                labelRes = R.string.progress,
                subline = stringResource(R.string.menu_progress_subline, state.stars),
                iconRes = R.drawable.ic_star,
                onClick = onProgress,
                modifier = Modifier.weight(1f)
            )
            RouteTile(
                labelRes = R.string.settings,
                subline = stringResource(R.string.menu_settings_subline),
                iconRes = R.drawable.ic_tune,
                onClick = onSettings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MenuStats(state: MenuUiState) {
    val items = listOf(
        StatItem(R.string.stat_best_win, state.bestWin, GoldLight, formatAmount(state.bestWin)),
        StatItem(R.string.stat_total_spins, state.totalSpins, Gold, formatAmount(state.totalSpins)),
        StatItem(
            R.string.stat_rooms_cleared,
            state.roomsCleared,
            Success,
            formatAmount(state.roomsCleared)
        )
    )
    if (items.count { it.amount > 0 } >= 2) {
        StatRow(items = items)
    } else {
        Text(
            text = stringResource(R.string.menu_record_hint),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
                .padding(vertical = 14.dp)
                .alpha(0.95f)
        )
    }
}
