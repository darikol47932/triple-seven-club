package com.Kiwdidi6.Dicrov.domain.usecase

import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.domain.model.LineWin
import com.Kiwdidi6.Dicrov.domain.model.PayLine
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

class EvaluatePayLinesUseCase {

    operator fun invoke(grid: List<List<SlotSymbol>>, bet: Int): List<LineWin> =
        PayLine.entries.mapNotNull { line ->
            val cells = List(GameConfig.REELS) { reel -> grid[reel][line.rows[reel]] }
            val symbol = resolve(cells) ?: return@mapNotNull null
            LineWin(line = line, symbol = symbol, chips = bet * symbol.payout)
        }

    private fun resolve(cells: List<SlotSymbol>): SlotSymbol? {
        val concrete = cells.filterNot { it.isWild }.distinct()
        return when (concrete.size) {
            0 -> SlotSymbol.WILD
            1 -> concrete.first()
            else -> null
        }
    }
}
