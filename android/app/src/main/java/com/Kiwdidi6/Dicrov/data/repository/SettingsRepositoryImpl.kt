package com.Kiwdidi6.Dicrov.data.repository

import com.Kiwdidi6.Dicrov.data.local.SettingsPreferences
import com.Kiwdidi6.Dicrov.domain.model.GameSettings
import com.Kiwdidi6.Dicrov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val preferences: SettingsPreferences
) : SettingsRepository {

    private val _settings = MutableStateFlow(preferences.load())
    override val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        val updated = transform(_settings.value)
        _settings.value = updated
        preferences.save(updated)
    }

    override fun reset() {
        preferences.clear()
        _settings.value = GameSettings()
        preferences.save(_settings.value)
    }
}
