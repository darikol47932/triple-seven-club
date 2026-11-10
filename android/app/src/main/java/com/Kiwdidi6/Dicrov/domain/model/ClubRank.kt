package com.Kiwdidi6.Dicrov.domain.model

enum class ClubRank(
    val starsRequired: Int,
    val extraSpins: Int,
    val shardBonusPercent: Int,
    val chargeCap: Int,
    val comboCap: Float
) {
    GUEST(0, 0, 0, 10, 2.0f),
    MEMBER(6, 1, 0, 10, 2.0f),
    REGULAR(12, 1, 10, 10, 2.0f),
    PATRON(18, 1, 10, 8, 2.0f),
    FOUNDER(24, 2, 10, 8, 2.5f);

    companion object {
        fun forStars(stars: Int): ClubRank = entries.last { stars >= it.starsRequired }
    }
}
