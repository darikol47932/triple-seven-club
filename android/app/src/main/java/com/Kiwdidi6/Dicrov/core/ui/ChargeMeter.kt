package com.Kiwdidi6.Dicrov.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.TextMuted

@Composable
fun ChargeMeter(
    charge: Int,
    cap: Int,
    reducedMotion: Boolean,
    modifier: Modifier = Modifier
) {
    val full = charge >= cap
    val transition = rememberInfiniteTransition(label = "charge_pulse")
    val pulse by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "charge_pulse_alpha"
    )
    val alpha = if (full && !reducedMotion) pulse else 1f
    val description = stringResource(R.string.cd_club_charge, charge, cap)
    Column(
        modifier = modifier.fillMaxWidth().semantics { stateDescription = description },
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(8.dp).alpha(alpha),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(cap) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (index < charge) {
                                Brush.horizontalGradient(listOf(Gold, GoldLight))
                            } else {
                                Brush.horizontalGradient(listOf(Color(0x1FFFFFFF), Color(0x1FFFFFFF)))
                            }
                        )
                )
            }
        }
        Text(
            text = stringResource(R.string.club_charge_label, charge, cap),
            style = MaterialTheme.typography.labelSmall,
            color = if (full) GoldLight else TextMuted
        )
    }
}
