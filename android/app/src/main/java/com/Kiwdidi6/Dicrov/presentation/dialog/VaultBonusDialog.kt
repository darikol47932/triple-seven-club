package com.Kiwdidi6.Dicrov.presentation.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.PrimaryButton
import com.Kiwdidi6.Dicrov.core.ui.formatAmount

@Composable
fun VaultBonusDialog(
    rewards: List<Int>,
    pickedIndex: Int,
    onPick: (Int) -> Unit,
    onContinue: () -> Unit
) {
    Dialog(onDismissRequest = onContinue) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(listOf(SurfaceWarm, BgBase)))
                .border(1.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.vault_title),
                style = MaterialTheme.typography.headlineSmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.picks_left, if (pickedIndex >= 0) 0 else 1),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rewards.forEachIndexed { index, reward ->
                    VaultDoor(
                        index = index,
                        reward = reward,
                        revealed = pickedIndex >= 0,
                        chosen = pickedIndex == index,
                        onPick = { onPick(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Text(
                text = stringResource(R.string.vault_hint),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                textRes = R.string.continue_label,
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun VaultDoor(
    index: Int,
    reward: Int,
    revealed: Boolean,
    chosen: Boolean,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (revealed) 180f else 0f,
        animationSpec = tween(420),
        label = "vault_door_flip"
    )
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .height(104.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(shape)
            .background(Brush.verticalGradient(listOf(Surface1, BgBase)))
            .border(1.dp, if (chosen) Gold else Gold.copy(alpha = 0.3f), shape)
            .alpha(if (revealed && !chosen) 0.45f else 1f)
            .clickable(enabled = !revealed, onClick = onPick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (revealed) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            ) {
                Text(
                    text = formatAmount(reward),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = GoldLight
                )
                Text(
                    text = stringResource(R.string.chips_label),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }
        } else {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(42.dp)
            )
            Box(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.vault_door, index + 1),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}
