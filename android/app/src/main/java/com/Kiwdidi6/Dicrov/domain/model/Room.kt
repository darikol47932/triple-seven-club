package com.Kiwdidi6.Dicrov.domain.model

data class Room(
    val index: Int,
    val nameRes: Int,
    val spins: Int,
    val targetPoints: Int,
    val featured: SlotSymbol
)
