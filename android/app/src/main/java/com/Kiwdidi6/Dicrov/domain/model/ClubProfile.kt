package com.Kiwdidi6.Dicrov.domain.model

import com.Kiwdidi6.Dicrov.core.config.GameConfig

data class ClubProfile(
    val chips: Int = GameConfig.STARTING_CHIPS,
    val unlockedRooms: Int = 1,
    val stars: Map<Int, Int> = emptyMap(),
    val bestScores: Map<Int, Int> = emptyMap(),
    val shards: Map<SlotSymbol, Int> = emptyMap(),
    val totalSpins: Int = 0,
    val bestWin: Int = 0,
    val bestStreak: Float = 1f,
    val topUpsUsed: Int = 0
) {
    val totalStars: Int
        get() = stars.values.sum()

    val rank: ClubRank
        get() = ClubRank.forStars(totalStars)

    val roomsCleared: Int
        get() = stars.count { it.value >= 2 }

    fun shardsOf(symbol: SlotSymbol): Int = shards[symbol] ?: 0

    fun isSymbolCollected(symbol: SlotSymbol): Boolean =
        shardsOf(symbol) >= GameConfig.SHARDS_PER_SYMBOL

    val collectedSymbols: Int
        get() = SlotSymbol.entries.count { isSymbolCollected(it) }
}
