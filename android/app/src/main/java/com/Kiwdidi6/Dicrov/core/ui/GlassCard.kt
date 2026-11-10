package com.Kiwdidi6.Dicrov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kiwdidi6.Dicrov.core.theme.GlassBorder
import com.Kiwdidi6.Dicrov.core.theme.GlassFill
import com.Kiwdidi6.Dicrov.core.theme.GlassGloss

val GlossBrush: Brush = Brush.verticalGradient(
    0f to GlassGloss,
    0.42f to Color.Transparent,
    1f to Color.Transparent
)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 18.dp,
    borderColor: Color = GlassBorder,
    fill: Color = GlassFill,
    contentPadding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Box(
        modifier = modifier
            .clip(shape)
            .background(fill)
            .border(1.dp, borderColor, shape)
    ) {
        Box(modifier = Modifier.matchParentSize().background(GlossBrush))
        Column(modifier = Modifier.fillMaxWidth().padding(contentPadding), content = content)
    }
}
