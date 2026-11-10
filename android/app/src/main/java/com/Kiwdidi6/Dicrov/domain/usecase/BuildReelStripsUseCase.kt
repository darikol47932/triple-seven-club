package com.Kiwdidi6.Dicrov.domain.usecase

import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.domain.model.Room
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.random.Random

class BuildReelStripsUseCase {

    operator fun invoke(room: Room, seed: Int): List<List<SlotSymbol>> {
        var attempt = 0
        while (attempt < GameConfig.LAYOUT_ATTEMPTS) {
            val strips = generate(room, Random(seed + attempt * GameConfig.STRIP_SEED_A))
            if (isTargetReachable(room, strips)) return strips
            attempt++
        }
        return fallback(room)
    }

    private fun generate(room: Room, random: Random): List<List<SlotSymbol>> =
        List(GameConfig.REELS) {
            val strip = mutableListOf<SlotSymbol>()
            SlotSymbol.entries.forEach { symbol ->
                val boost = if (symbol == room.featured) GameConfig.FEATURED_WEIGHT_BOOST else 1f
                val count = (symbol.weight * boost).roundToInt().coerceAtLeast(1)
                repeat(count) { strip.add(symbol) }
            }
            strip.shuffle(random)
            strip.toList()
        }

    private fun isTargetReachable(room: Room, strips: List<List<SlotSymbol>>): Boolean {
        val shared = SlotSymbol.entries.filter { symbol ->
            strips.all { strip -> strip.contains(symbol) || strip.contains(SlotSymbol.WILD) }
        }
        val bestPayout = shared.maxOfOrNull { it.payout } ?: return false
        val bestPointsPerSpin = floor(GameConfig.BET * bestPayout / 2.0).toInt()
        return bestPointsPerSpin * room.spins >= room.targetPoints
    }

    private fun fallback(room: Room): List<List<SlotSymbol>> =
        List(GameConfig.REELS) { reel ->
            val strip = mutableListOf<SlotSymbol>()
            SlotSymbol.entries.forEach { symbol ->
                val boost = if (symbol == room.featured) GameConfig.FEATURED_WEIGHT_BOOST else 1f
                val count = (symbol.weight * boost).roundToInt().coerceAtLeast(1)
                repeat(count) { strip.add(symbol) }
            }
            val rotation = (reel + 1) * 7
            List(strip.size) { position -> strip[(position + rotation) % strip.size] }
        }
}
