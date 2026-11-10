package com.Kiwdidi6.Dicrov.domain.model

data class Decoration(
    val id: Int,
    val nameRes: Int,
    val requirementRes: Int,
    val symbol: SlotSymbol,
    val rankRequired: ClubRank?
)
