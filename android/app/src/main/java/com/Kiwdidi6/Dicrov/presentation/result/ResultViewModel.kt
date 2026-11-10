package com.Kiwdidi6.Dicrov.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.RoomOutcome
import com.Kiwdidi6.Dicrov.domain.model.RoomResult
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ResultViewModel(
    private val club: ClubRepository
) : ViewModel() {

    val state: StateFlow<ResultUiState> = combine(
        club.lastResult,
        club.profile
    ) { result, profile -> toUiState(result, profile) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(club.lastResult.value, club.profile.value)
        )

    fun onPlayAgain(): Int {
        val current = state.value
        if (current.outcome == RoomOutcome.OUT_OF_CHIPS) {
            club.update { profile ->
                profile.copy(
                    chips = profile.chips + GameConfig.TOP_UP_CHIPS,
                    topUpsUsed = profile.topUpsUsed + 1
                )
            }
        }
        return current.nextRoomIndex
    }

    private fun toUiState(result: RoomResult?, profile: ClubProfile): ResultUiState {
        if (result == null) {
            val room = SampleData.roomAt(0)
            return ResultUiState(
                roomNameRes = room.nameRes,
                target = room.targetPoints,
                spinBudget = room.spins,
                chips = profile.chips
            )
        }
        val room = SampleData.roomAt(result.roomIndex)
        val next = if (result.outcome == RoomOutcome.CLEARED) {
            (result.roomIndex + 1).coerceAtMost(SampleData.rooms.lastIndex)
        } else {
            result.roomIndex
        }
        val decoration = SampleData.decorations.firstOrNull { it.id == result.unlockedDecorationId }
        return ResultUiState(
            roomIndex = result.roomIndex,
            roomNameRes = room.nameRes,
            outcome = result.outcome,
            points = result.points,
            target = result.target,
            chipsWon = result.chipsWon,
            shardsGained = result.shardsGained,
            stars = result.stars,
            bestStreak = result.bestStreak,
            spinsUsed = result.spinsUsed,
            spinBudget = result.spinBudget,
            unlockedDecorationNameRes = decoration?.nameRes ?: 0,
            unlockedDecorationSymbol = decoration?.symbol,
            nextRoomIndex = next,
            chips = profile.chips
        )
    }
}
