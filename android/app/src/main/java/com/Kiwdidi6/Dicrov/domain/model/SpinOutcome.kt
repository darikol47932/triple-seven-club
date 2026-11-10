package com.Kiwdidi6.Dicrov.domain.model

data class LineWin(
    val line: PayLine,
    val symbol: SlotSymbol,
    val chips: Int
)

data class SpinOutcome(
    val stops: List<Int>,
    val grid: List<List<SlotSymbol>>,
    val wins: List<LineWin>,
    val chipsWon: Int,
    val forcedWild: Boolean
) {
    val isWin: Boolean
        get() = chipsWon > 0
}
