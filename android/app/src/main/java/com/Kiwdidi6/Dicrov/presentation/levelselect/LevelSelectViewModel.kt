package com.Kiwdidi6.Dicrov.presentation.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LevelSelectViewModel(
    private val club: ClubRepository
) : ViewModel() {

    val state: StateFlow<LevelSelectUiState> = club.profile
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(club.profile.value)
        )

    private fun toUiState(profile: ClubProfile): LevelSelectUiState = LevelSelectUiState(
        chips = profile.chips,
        rooms = SampleData.rooms.map { room ->
            RoomCardState(
                index = room.index,
                nameRes = room.nameRes,
                spins = room.spins + profile.rank.extraSpins,
                targetPoints = room.targetPoints,
                featured = room.featured,
                unlocked = room.index < profile.unlockedRooms,
                stars = profile.stars[room.index] ?: 0,
                bestScore = profile.bestScores[room.index] ?: 0
            )
        }
    )
}
