package com.Kiwdidi6.Dicrov.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val club: ClubRepository
) : ViewModel() {

    val state: StateFlow<MenuUiState> = club.profile
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(club.profile.value)
        )

    private fun toUiState(profile: ClubProfile): MenuUiState {
        val nextIndex = (profile.unlockedRooms - 1).coerceIn(0, SampleData.rooms.lastIndex)
        val room = SampleData.roomAt(nextIndex)
        return MenuUiState(
            chips = profile.chips,
            nextRoomIndex = nextIndex,
            nextRoomNameRes = room.nameRes,
            nextRoomSpins = room.spins + profile.rank.extraSpins,
            nextRoomTarget = room.targetPoints,
            unlockedRooms = profile.unlockedRooms,
            collectedSymbols = profile.collectedSymbols,
            stars = profile.totalStars,
            rank = profile.rank,
            bestWin = profile.bestWin,
            totalSpins = profile.totalSpins,
            roomsCleared = profile.roomsCleared
        )
    }
}
