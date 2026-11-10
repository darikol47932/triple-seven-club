package com.Kiwdidi6.Dicrov.domain.model

enum class ReelSpeed(val factor: Float) {
    RELAXED(1.35f),
    STANDARD(1.0f),
    FAST(0.70f)
}

enum class WinCelebration { FULL, SHORT }

data class GameSettings(
    val reelSpeed: ReelSpeed = ReelSpeed.STANDARD,
    val celebration: WinCelebration = WinCelebration.FULL,
    val reducedMotion: Boolean = false,
    val payLineHints: Boolean = true
)
