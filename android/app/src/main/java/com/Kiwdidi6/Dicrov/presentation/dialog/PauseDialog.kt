package com.Kiwdidi6.Dicrov.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.SurfaceWarm
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.PrimaryButton
import com.Kiwdidi6.Dicrov.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    roomNameRes: Int,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onEndLevel: () -> Unit,
    onMenu: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(listOf(SurfaceWarm, BgBase)))
                .border(1.dp, Gold.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineSmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(roomNameRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                textRes = R.string.resume,
                onClick = onResume,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                textRes = R.string.restart_room,
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                textRes = R.string.end_level,
                onClick = onEndLevel,
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                textRes = R.string.menu,
                onClick = onMenu,
                spriteRes = R.drawable.icon_home,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
