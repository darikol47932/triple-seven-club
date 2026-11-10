package com.Kiwdidi6.Dicrov.presentation.result

import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.domain.model.RoomOutcome
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

data class ResultUiState(
    val roomIndex: Int = 0,
    val roomNameRes: Int = R.string.room_velvet_lounge,
    val outcome: RoomOutcome = RoomOutcome.FAILED,
    val points: Int = 0,
    val target: Int = 120,
    val chipsWon: Int = 0,
    val shardsGained: Int = 0,
    val stars: Int = 0,
    val bestStreak: Float = 1f,
    val spinsUsed: Int = 0,
    val spinBudget: Int = 8,
    val unlockedDecorationNameRes: Int = 0,
    val unlockedDecorationSymbol: SlotSymbol? = null,
    val nextRoomIndex: Int = 0,
    val chips: Int = 0
)
