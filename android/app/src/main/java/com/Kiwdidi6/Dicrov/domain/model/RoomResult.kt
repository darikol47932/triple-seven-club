package com.Kiwdidi6.Dicrov.domain.model

enum class RoomOutcome { CLEARED, FAILED, OUT_OF_CHIPS }

data class RoomResult(
    val roomIndex: Int,
    val outcome: RoomOutcome,
    val points: Int,
    val target: Int,
    val chipsWon: Int,
    val shardsGained: Int,
    val stars: Int,
    val bestStreak: Float,
    val spinsUsed: Int,
    val spinBudget: Int,
    val unlockedDecorationId: Int
)
