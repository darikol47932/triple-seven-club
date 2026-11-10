package com.Kiwdidi6.Dicrov.domain.repository

import com.Kiwdidi6.Dicrov.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>

    fun update(transform: (GameSettings) -> GameSettings)
    fun reset()
}
