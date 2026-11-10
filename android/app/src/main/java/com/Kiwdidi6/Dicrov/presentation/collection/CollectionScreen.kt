package com.Kiwdidi6.Dicrov.presentation.collection

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.ScreenScaffold
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile
import com.Kiwdidi6.Dicrov.core.ui.formatRatio
import com.Kiwdidi6.Dicrov.core.ui.nameRes

@Composable
fun CollectionScreen(onMenu: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(
        titleRes = R.string.symbol_collection,
        onMenu = onMenu,
        trailing = {
            Text(
                text = formatRatio(state.collectedCount, state.totalSymbols),
                style = MaterialTheme.typography.labelLarge,
                color = GoldLight
            )
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            if (!state.anyShards) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = stringResource(R.string.collection_empty_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x14FFFFFF))
                            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
                            .padding(vertical = 14.dp, horizontal = 10.dp)
                    )
                }
            }
            items(state.symbols, key = { it.symbol.name }) { card ->
                SymbolCard(card = card)
            }
        }
    }
}

@Composable
private fun SymbolCard(card: SymbolCardState) {
    val shape = RoundedCornerShape(16.dp)
    val description = if (card.collected) {
        stringResource(R.string.state_collected)
    } else {
        stringResource(R.string.shards_of, card.shards, card.required)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.verticalGradient(listOf(SurfaceWarm, Surface1)))
            .border(1.dp, if (card.collected) Gold else Color(0x1FFFFFFF), shape)
            .semantics { stateDescription = description }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        SymbolTile(
            symbol = card.symbol,
            size = 72.dp,
            dimmed = !card.collected && card.shards == 0,
            plate = false
        )
        Text(
            text = stringResource(card.symbol.nameRes()),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 13.sp,
            color = GoldLight,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.pays_multiplier, card.symbol.payout),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextMuted
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
        ) {
            val fraction = (card.shards.toFloat() / card.required.toFloat()).coerceIn(0f, 1f)
            if (fraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(Gold, GoldLight)))
                )
            }
        }
        Text(
            text = stringResource(R.string.shards_of, card.shards, card.required),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextSecondary
        )
        if (card.collected) {
            Text(
                text = stringResource(R.string.state_collected),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Gold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x1AD4AF37))
                    .padding(horizontal = 9.dp, vertical = 3.dp)
            )
        } else if (card.rewardNameRes != 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.unlocks_decoration,
                        stringResource(card.rewardNameRes)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
