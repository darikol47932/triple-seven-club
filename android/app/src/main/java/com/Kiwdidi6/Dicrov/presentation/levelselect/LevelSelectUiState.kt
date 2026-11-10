package com.Kiwdidi6.Dicrov.presentation.levelselect

import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

data class RoomCardState(
    val index: Int,
    val nameRes: Int,
    val spins: Int,
    val targetPoints: Int,
    val featured: SlotSymbol,
    val unlocked: Boolean,
    val stars: Int,
    val bestScore: Int
)

data class LevelSelectUiState(
    val rooms: List<RoomCardState> = emptyList(),
    val chips: Int = 0
)
