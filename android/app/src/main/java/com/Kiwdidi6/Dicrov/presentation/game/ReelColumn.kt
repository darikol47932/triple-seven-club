package com.Kiwdidi6.Dicrov.presentation.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.Kiwdidi6.Dicrov.core.ui.SymbolTile
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun ReelColumn(
    strip: List<SlotSymbol>,
    offset: Float,
    cell: Dp,
    rows: Int,
    reelIndex: Int,
    winningCells: Set<Int>,
    winScale: Float,
    modifier: Modifier = Modifier
) {
    if (strip.isEmpty()) return
    val cellPx = with(LocalDensity.current) { cell.toPx() }
    val base = floor(offset).toInt()
    val fraction = offset - base
    val atRest = fraction < 0.001f

    Box(
        modifier = modifier
            .width(cell)
            .height(cell * rows)
            .clipToBounds()
    ) {
        for (slot in 0..rows) {
            val index = ((base + slot) % strip.size + strip.size) % strip.size
            val shift = ((slot - fraction) * cellPx).roundToInt()
            val highlighted = atRest &&
                slot < rows &&
                winningCells.contains(reelIndex * rows + slot)
            SymbolTile(
                symbol = strip[index],
                size = cell,
                highlighted = highlighted,
                scale = if (highlighted) winScale else 1f,
                modifier = Modifier.offset { IntOffset(0, shift) }
            )
        }
    }
}
