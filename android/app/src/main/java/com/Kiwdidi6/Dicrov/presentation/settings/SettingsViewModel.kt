package com.Kiwdidi6.Dicrov.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import com.Kiwdidi6.Dicrov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val club: ClubRepository
) : ViewModel() {

    private val dialogVisible = MutableStateFlow(false)

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        dialogVisible
    ) { settings, dialog ->
        SettingsUiState(
            reelSpeed = settings.reelSpeed,
            celebration = settings.celebration,
            reducedMotion = settings.reducedMotion,
            payLineHints = settings.payLineHints,
            showResetDialog = dialog
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun selectReelSpeed(speed: ReelSpeed) {
        settingsRepository.update { it.copy(reelSpeed = speed) }
    }

    fun selectCelebration(celebration: WinCelebration) {
        settingsRepository.update { it.copy(celebration = celebration) }
    }

    fun setReducedMotion(enabled: Boolean) {
        settingsRepository.update { it.copy(reducedMotion = enabled) }
    }

    fun setPayLineHints(enabled: Boolean) {
        settingsRepository.update { it.copy(payLineHints = enabled) }
    }

    fun openResetDialog() {
        dialogVisible.update { true }
    }

    fun dismissResetDialog() {
        dialogVisible.update { false }
    }

    fun confirmReset() {
        club.reset()
        settingsRepository.reset()
        dialogVisible.update { false }
    }
}
