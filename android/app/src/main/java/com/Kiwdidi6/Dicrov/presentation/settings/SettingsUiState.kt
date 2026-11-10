package com.Kiwdidi6.Dicrov.presentation.settings

import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration

data class SettingsUiState(
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val celebration: WinCelebration = WinCelebration.FULL,
    val reducedMotion: Boolean = false,
    val payLineHints: Boolean = true,
    val showResetDialog: Boolean = false
)
