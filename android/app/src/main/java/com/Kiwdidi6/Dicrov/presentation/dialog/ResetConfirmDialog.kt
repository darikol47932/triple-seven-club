package com.Kiwdidi6.Dicrov.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.theme.Danger
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.Surface1
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary

@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface1,
        titleContentColor = GoldLight,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.reset_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_message),
                style = MaterialTheme.typography.bodySmall
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.reset), color = Danger)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel), color = GoldLight)
            }
        }
    )
}
