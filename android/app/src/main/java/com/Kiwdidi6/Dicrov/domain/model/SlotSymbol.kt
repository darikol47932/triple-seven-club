package com.Kiwdidi6.Dicrov.domain.model

enum class SlotSymbol(val key: String, val payout: Int, val weight: Int) {
    CHERRY("cherry", 3, 30),
    BAR("bar", 5, 22),
    BELL("bell", 8, 14),
    DIAMOND("diamond", 20, 7),
    SEVEN("seven", 50, 3),
    WILD("wild", 60, 5);

    val isWild: Boolean
        get() = this == WILD

    companion object {
        fun fromKey(key: String): SlotSymbol? = entries.firstOrNull { it.key == key }
    }
}
