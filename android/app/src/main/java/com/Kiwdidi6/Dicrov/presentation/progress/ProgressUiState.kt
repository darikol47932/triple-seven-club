package com.Kiwdidi6.Dicrov.presentation.progress

import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubRank
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

data class RankNodeState(
    val rank: ClubRank,
    val reached: Boolean,
    val current: Boolean
)

data class DecorationState(
    val id: Int,
    val nameRes: Int,
    val requirementRes: Int,
    val symbol: SlotSymbol,
    val unlocked: Boolean
)

data class RoomHistoryState(
    val index: Int,
    val nameRes: Int,
    val stars: Int,
    val bestScore: Int,
    val played: Boolean
)

data class ProgressUiState(
    val ranks: List<RankNodeState> = emptyList(),
    val currentRank: ClubRank = ClubRank.GUEST,
    val stars: Int = 0,
    val maxStars: Int = SampleData.maxStars,
    val roomsCleared: Int = 0,
    val totalRooms: Int = SampleData.rooms.size,
    val totalSpins: Int = 0,
    val bestWin: Int = 0,
    val bestStreak: Float = 1f,
    val bestRoomScore: Int = 0,
    val decorations: List<DecorationState> = emptyList(),
    val history: List<RoomHistoryState> = emptyList()
)
