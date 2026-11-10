package com.Kiwdidi6.Dicrov.domain.usecase

import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import com.Kiwdidi6.Dicrov.domain.model.SpinOutcome
import kotlin.random.Random

class SpinReelsUseCase(
    private val evaluatePayLines: EvaluatePayLinesUseCase
) {

    operator fun invoke(
        strips: List<List<SlotSymbol>>,
        random: Random,
        bet: Int,
        pityActive: Boolean,
        forceWild: Boolean,
        suppressBigWin: Boolean
    ): SpinOutcome {
        var stops = rollStops(strips, random, pityActive)
        if (forceWild) stops = withMiddleWild(strips, stops, random)
        var grid = gridOf(strips, stops)
        var wins = evaluatePayLines(grid, bet)
        var total = wins.sumOf { it.chips }

        if (suppressBigWin && total >= bet * GameConfig.BIG_WIN_MULTIPLE && !forceWild) {
            stops = rollStops(strips, random, false)
            grid = gridOf(strips, stops)
            wins = evaluatePayLines(grid, bet)
            total = wins.sumOf { it.chips }
        }

        return SpinOutcome(
            stops = stops,
            grid = grid,
            wins = wins,
            chipsWon = total,
            forcedWild = forceWild
        )
    }

    fun gridOf(strips: List<List<SlotSymbol>>, stops: List<Int>): List<List<SlotSymbol>> =
        List(GameConfig.REELS) { reel ->
            val strip = strips[reel]
            List(GameConfig.ROWS) { row -> strip[(stops[reel] + row) % strip.size] }
        }

    private fun rollStops(
        strips: List<List<SlotSymbol>>,
        random: Random,
        pityActive: Boolean
    ): List<Int> = strips.map { strip ->
        val first = random.nextInt(strip.size)
        if (!pityActive) return@map first
        val second = random.nextInt(strip.size)
        val firstIsWild = strip[(first + 1) % strip.size].isWild
        val secondIsWild = strip[(second + 1) % strip.size].isWild
        if (!firstIsWild && secondIsWild) second else first
    }

    private fun withMiddleWild(
        strips: List<List<SlotSymbol>>,
        stops: List<Int>,
        random: Random
    ): List<Int> {
        if (stops.indices.any { strips[it][(stops[it] + 1) % strips[it].size].isWild }) return stops
        val reel = random.nextInt(GameConfig.REELS)
        val strip = strips[reel]
        val wildIndex = strip.indices.firstOrNull { strip[it].isWild } ?: return stops
        val target = ((wildIndex - 1) % strip.size + strip.size) % strip.size
        return stops.mapIndexed { index, stop -> if (index == reel) target else stop }
    }
}
