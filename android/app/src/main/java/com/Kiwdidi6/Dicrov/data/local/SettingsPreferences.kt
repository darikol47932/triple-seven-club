package com.Kiwdidi6.Dicrov.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Kiwdidi6.Dicrov.domain.model.GameSettings
import com.Kiwdidi6.Dicrov.domain.model.ReelSpeed
import com.Kiwdidi6.Dicrov.domain.model.WinCelebration

class SettingsPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun load(): GameSettings = GameSettings(
        reelSpeed = readSpeed(prefs.getString(KEY_SPEED, null)),
        celebration = readCelebration(prefs.getString(KEY_CELEBRATION, null)),
        reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, false),
        payLineHints = prefs.getBoolean(KEY_PAY_LINES, true)
    )

    fun save(settings: GameSettings) {
        prefs.edit()
            .putString(KEY_SPEED, settings.reelSpeed.name)
            .putString(KEY_CELEBRATION, settings.celebration.name)
            .putBoolean(KEY_REDUCED_MOTION, settings.reducedMotion)
            .putBoolean(KEY_PAY_LINES, settings.payLineHints)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun readSpeed(raw: String?): ReelSpeed =
        ReelSpeed.entries.firstOrNull { it.name == raw } ?: ReelSpeed.STANDARD

    private fun readCelebration(raw: String?): WinCelebration =
        WinCelebration.entries.firstOrNull { it.name == raw } ?: WinCelebration.FULL

    private companion object {
        const val STORE = "club_settings"
        const val KEY_SPEED = "reel_speed"
        const val KEY_CELEBRATION = "celebration"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        const val KEY_PAY_LINES = "pay_line_hints"
    }
}
