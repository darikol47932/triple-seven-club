package com.Kiwdidi6.Dicrov.data.sample

import com.Kiwdidi6.Dicrov.R
import com.Kiwdidi6.Dicrov.domain.model.ClubRank
import com.Kiwdidi6.Dicrov.domain.model.Decoration
import com.Kiwdidi6.Dicrov.domain.model.Room
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

object SampleData {

    val rooms: List<Room> = listOf(
        Room(0, R.string.room_velvet_lounge, 8, 120, SlotSymbol.CHERRY),
        Room(1, R.string.room_brass_parlour, 9, 180, SlotSymbol.BAR),
        Room(2, R.string.room_neon_atrium, 10, 250, SlotSymbol.BELL),
        Room(3, R.string.room_gilded_hall, 10, 330, SlotSymbol.DIAMOND),
        Room(4, R.string.room_midnight_salon, 11, 420, SlotSymbol.SEVEN),
        Room(5, R.string.room_chrome_terrace, 12, 520, SlotSymbol.BELL),
        Room(6, R.string.room_royal_vault, 13, 640, SlotSymbol.DIAMOND),
        Room(7, R.string.room_triple_seven_club, 14, 800, SlotSymbol.SEVEN)
    )

    val decorations: List<Decoration> = listOf(
        Decoration(0, R.string.decor_chrome_marquee, R.string.decor_req_cherry, SlotSymbol.CHERRY, null),
        Decoration(1, R.string.decor_velvet_rope, R.string.decor_req_bar, SlotSymbol.BAR, null),
        Decoration(2, R.string.decor_crystal_chandelier, R.string.decor_req_bell, SlotSymbol.BELL, null),
        Decoration(3, R.string.decor_brass_fountain, R.string.decor_req_diamond, SlotSymbol.DIAMOND, null),
        Decoration(4, R.string.decor_seven_arch, R.string.decor_req_seven, SlotSymbol.SEVEN, null),
        Decoration(5, R.string.decor_founders_plaque, R.string.decor_req_founder, SlotSymbol.WILD, ClubRank.FOUNDER)
    )

    val vaultRewards: List<Int> = listOf(50, 120, 300)

    val maxStars: Int = rooms.size * 3

    fun roomAt(index: Int): Room = rooms[index.coerceIn(0, rooms.lastIndex)]
}
