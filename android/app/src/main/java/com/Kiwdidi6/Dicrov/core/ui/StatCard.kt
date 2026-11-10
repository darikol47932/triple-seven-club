package com.Kiwdidi6.Dicrov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kiwdidi6.Dicrov.core.theme.GlassBorder
import com.Kiwdidi6.Dicrov.core.theme.GlassFill
import com.Kiwdidi6.Dicrov.core.theme.NumberStyle
import com.Kiwdidi6.Dicrov.core.theme.TextMuted

data class StatItem(
    val labelRes: Int,
    val amount: Int,
    val accent: Color,
    val display: String
)

@Composable
fun StatCard(
    labelRes: Int,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(GlassFill)
            .border(1.dp, GlassBorder, shape)
    ) {
        Box(modifier = Modifier.matchParentSize().background(GlossBrush))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Text(
                text = value,
                style = NumberStyle,
                color = accent,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatRow(
    items: List<StatItem>,
    modifier: Modifier = Modifier
) {
    val visible = items.filter { it.amount > 0 }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visible.forEach { item ->
            StatCard(
                labelRes = item.labelRes,
                value = item.display,
                accent = item.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
