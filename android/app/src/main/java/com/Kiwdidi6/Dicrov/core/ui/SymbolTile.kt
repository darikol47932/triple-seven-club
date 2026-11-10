package com.Kiwdidi6.Dicrov.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kiwdidi6.Dicrov.core.theme.GlassBorder
import com.Kiwdidi6.Dicrov.core.theme.GoldWash
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

@Composable
fun SymbolTile(
    symbol: SlotSymbol,
    size: Dp,
    modifier: Modifier = Modifier,
    dimmed: Boolean = false,
    highlighted: Boolean = false,
    plate: Boolean = true,
    scale: Float = 1f
) {
    val shape = RoundedCornerShape(12.dp)
    val fill = when {
        highlighted -> GoldWash
        plate -> Color(0x0DFFFFFF)
        else -> Color.Transparent
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(fill)
            .border(1.dp, if (highlighted) GlassBorder else Color(0x12FFFFFF), shape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(symbol.spriteRes()),
            contentDescription = stringResource(symbol.nameRes()),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(size * 0.09f)
                .size(size * 0.78f * scale)
                .alpha(if (dimmed) 0.5f else 1f)
        )
    }
}
