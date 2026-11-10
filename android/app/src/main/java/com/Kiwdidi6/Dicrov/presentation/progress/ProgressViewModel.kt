package com.Kiwdidi6.Dicrov.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.ClubRank
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(
    private val club: ClubRepository
) : ViewModel() {

    val state: StateFlow<ProgressUiState> = club.profile
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(club.profile.value)
        )

    private fun toUiState(profile: ClubProfile): ProgressUiState {
        val rank = profile.rank
        return ProgressUiState(
            ranks = ClubRank.entries.map { entry ->
                RankNodeState(
                    rank = entry,
                    reached = profile.totalStars >= entry.starsRequired,
                    current = entry == rank
                )
            },
            currentRank = rank,
            stars = profile.totalStars,
            roomsCleared = profile.roomsCleared,
            totalSpins = profile.totalSpins,
            bestWin = profile.bestWin,
            bestStreak = profile.bestStreak,
            bestRoomScore = profile.bestScores.values.maxOrNull() ?: 0,
            decorations = SampleData.decorations.map { decoration ->
                DecorationState(
                    id = decoration.id,
                    nameRes = decoration.nameRes,
                    requirementRes = decoration.requirementRes,
                    symbol = decoration.symbol,
                    unlocked = if (decoration.rankRequired != null) {
                        profile.totalStars >= decoration.rankRequired.starsRequired
                    } else {
                        profile.isSymbolCollected(decoration.symbol)
                    }
                )
            },
            history = SampleData.rooms.map { room ->
                RoomHistoryState(
                    index = room.index,
                    nameRes = room.nameRes,
                    stars = profile.stars[room.index] ?: 0,
                    bestScore = profile.bestScores[room.index] ?: 0,
                    played = profile.bestScores.containsKey(room.index)
                )
            }
        )
    }
}
