package com.Kiwdidi6.Dicrov.presentation.menu

import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.data.sample.SampleData
import com.Kiwdidi6.Dicrov.domain.model.ClubRank
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

data class MenuUiState(
    val chips: Int = GameConfig.STARTING_CHIPS,
    val nextRoomIndex: Int = 0,
    val nextRoomNameRes: Int = R.string.room_velvet_lounge,
    val nextRoomSpins: Int = 8,
    val nextRoomTarget: Int = 120,
    val unlockedRooms: Int = 1,
    val totalRooms: Int = SampleData.rooms.size,
    val collectedSymbols: Int = 0,
    val totalSymbols: Int = SlotSymbol.entries.size,
    val stars: Int = 0,
    val maxStars: Int = SampleData.maxStars,
    val rank: ClubRank = ClubRank.GUEST,
    val bestWin: Int = 0,
    val totalSpins: Int = 0,
    val roomsCleared: Int = 0
)
