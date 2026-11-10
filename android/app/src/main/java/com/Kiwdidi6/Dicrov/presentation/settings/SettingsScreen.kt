package com.Kiwdidi6.Dicrov.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.core.theme.Danger
import com.Kiwdidi6.Dicrov.core.theme.Gold
import com.Kiwdidi6.Dicrov.core.theme.GoldLight
import com.Kiwdidi6.Dicrov.core.theme.TextMuted
import com.Kiwdidi6.Dicrov.core.theme.TextSecondary
import com.Kiwdidi6.Dicrov.core.ui.GlassCard
import com.Kiwdidi6.Dicrov.core.ui.ScreenScaffold
import com.Kiwdidi6.Dicrov.core.ui.SecondaryButton
import com.Kiwdidi6.Dicrov.core.ui.labelRes
import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration
import com.Kiwdidi6.Dicrov.presentation.dialog.ResetConfirmDialog

@Composable
fun SettingsScreen(onMenu: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(titleRes = R.string.club_settings, onMenu = onMenu) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OptionTitle(
                        titleRes = R.string.setting_reel_speed,
                        descriptionRes = R.string.setting_reel_speed_hint
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ReelSpeed.entries.forEach { speed ->
                            SegmentChip(
                                labelRes = speed.labelRes(),
                                selected = state.reelSpeed == speed,
                                onClick = { viewModel.selectReelSpeed(speed) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OptionTitle(
                        titleRes = R.string.setting_celebration,
                        descriptionRes = R.string.setting_celebration_hint
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        WinCelebration.entries.forEach { option ->
                            SegmentChip(
                                labelRes = option.labelRes(),
                                selected = state.celebration == option,
                                onClick = { viewModel.selectCelebration(option) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SwitchRow(
                        titleRes = R.string.setting_reduced_motion,
                        descriptionRes = R.string.setting_reduced_motion_hint,
                        checked = state.reducedMotion,
                        onChange = { viewModel.setReducedMotion(it) }
                    )
                    SwitchRow(
                        titleRes = R.string.setting_pay_lines,
                        descriptionRes = R.string.setting_pay_lines_hint,
                        checked = state.payLineHints,
                        onChange = { viewModel.setPayLineHints(it) }
                    )
                }
            }

            SecondaryButton(
                textRes = R.string.reset_progress,
                onClick = { viewModel.openResetDialog() },
                contentColor = Danger,
                borderColor = Danger.copy(alpha = 0.6f),
                height = 52.dp,
                modifier = Modifier.fillMaxWidth()
            )

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.responsible_title),
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(R.string.responsible_body),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(R.string.responsible_chips),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (state.showResetDialog) {
        ResetConfirmDialog(
            onConfirm = { viewModel.confirmReset() },
            onDismiss = { viewModel.dismissResetDialog() }
        )
    }
}

@Composable
private fun OptionTitle(titleRes: Int, descriptionRes: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            color = GoldLight
        )
        Text(
            text = stringResource(descriptionRes),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun SegmentChip(
    labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    val description = stringResource(
        if (selected) R.string.state_on else R.string.state_off
    )
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (selected) Gold else Color(0x14FFFFFF))
            .border(1.dp, if (selected) GoldLight else Color(0x1FFFFFFF), shape)
            .semantics { stateDescription = description }
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = if (selected) BgBase else TextSecondary
        )
    }
}

@Composable
private fun SwitchRow(
    titleRes: Int,
    descriptionRes: Int,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    val description = stringResource(if (checked) R.string.state_on else R.string.state_off)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                color = GoldLight
            )
            Text(
                text = stringResource(descriptionRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BgBase,
                checkedTrackColor = Gold,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color(0x1FFFFFFF)
            ),
            modifier = Modifier.semantics { stateDescription = description }
        )
    }
}
