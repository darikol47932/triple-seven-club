package com.Kiwdidi6.Dicrov.data.repository

import com.Kiwdidi6.Dicrov.data.local.ClubPreferences
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.RoomResult
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClubRepositoryImpl(
    private val preferences: ClubPreferences
) : ClubRepository {

    private val _profile = MutableStateFlow(preferences.load())
    override val profile: StateFlow<ClubProfile> = _profile.asStateFlow()

    private val _lastResult = MutableStateFlow<RoomResult?>(null)
    override val lastResult: StateFlow<RoomResult?> = _lastResult.asStateFlow()

    override fun update(transform: (ClubProfile) -> ClubProfile) {
        val updated = transform(_profile.value)
        _profile.value = updated
        preferences.save(updated)
    }

    override fun publishResult(result: RoomResult) {
        _lastResult.value = result
    }

    override fun reset() {
        preferences.clear()
        _profile.value = ClubProfile()
        preferences.save(_profile.value)
        _lastResult.value = null
    }
}
