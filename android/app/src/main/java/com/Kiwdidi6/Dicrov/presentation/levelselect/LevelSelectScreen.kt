package com.Kiwdidi6.Dicrov.presentation.levelselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldDim
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.ScreenScaffold
import com.Kiwdidi6.Dicrov.core.ui.SecondaryButton
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile

@Composable
fun LevelSelectScreen(
    onMenu: () -> Unit,
    onEnterRoom: (Int) -> Unit
) {
    val viewModel: LevelSelectViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(titleRes = R.string.club_rooms, onMenu = onMenu) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(state.rooms, key = { it.index }) { room ->
                RoomCard(room = room, onEnter = { onEnterRoom(room.index) })
            }
        }
    }
}

@Composable
private fun RoomCard(room: RoomCardState, onEnter: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    val lockedLabel = stringResource(R.string.locked)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.verticalGradient(listOf(SurfaceWarm, Surface1)))
            .border(1.dp, if (room.unlocked) Gold.copy(alpha = 0.4f) else Color(0x1FFFFFFF), shape)
            .alpha(if (room.unlocked) 1f else 0.45f)
            .then(
                if (room.unlocked) Modifier else Modifier.semantics { stateDescription = lockedLabel }
            )
            .padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.room_index, room.index + 1),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
            if (room.unlocked) {
                SymbolTile(symbol = room.featured, size = 40.dp, plate = false)
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Text(
            text = stringResource(room.nameRes),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            color = GoldLight
        )
        Text(
            text = stringResource(R.string.room_goal_short, room.targetPoints, room.spins),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = TextSecondary
        )
        StarRow(stars = room.stars)
        Box(modifier = Modifier.fillMaxWidth().height(4.dp))
        if (room.unlocked) {
            SecondaryButton(
                textRes = R.string.enter,
                onClick = onEnter,
                height = 44.dp,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = stringResource(R.string.room_requirement, room.index),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x14FFFFFF))
                    .padding(vertical = 13.dp, horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun StarRow(stars: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { index ->
            Icon(
                painter = painterResource(
                    if (index < stars) R.drawable.ic_star else R.drawable.ic_star_outline
                ),
                contentDescription = null,
                tint = if (index < stars) GoldLight else GoldDim,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
