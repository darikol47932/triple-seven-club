package com.Kiwdidi6.Dicrov.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Kiwdidi6.Dicrov.core.config.GameConfig
import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.SlotSymbol

class ClubPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun load(): ClubProfile = ClubProfile(
        chips = prefs.getInt(KEY_CHIPS, GameConfig.STARTING_CHIPS),
        unlockedRooms = prefs.getInt(KEY_UNLOCKED, 1),
        stars = readIntMap(prefs.getString(KEY_STARS, null)),
        bestScores = readIntMap(prefs.getString(KEY_BEST_SCORES, null)),
        shards = readShards(prefs.getString(KEY_SHARDS, null)),
        totalSpins = prefs.getInt(KEY_TOTAL_SPINS, 0),
        bestWin = prefs.getInt(KEY_BEST_WIN, 0),
        bestStreak = prefs.getFloat(KEY_BEST_STREAK, 1f),
        topUpsUsed = prefs.getInt(KEY_TOP_UPS, 0)
    )

    fun save(profile: ClubProfile) {
        prefs.edit()
            .putInt(KEY_CHIPS, profile.chips)
            .putInt(KEY_UNLOCKED, profile.unlockedRooms)
            .putString(KEY_STARS, writeIntMap(profile.stars))
            .putString(KEY_BEST_SCORES, writeIntMap(profile.bestScores))
            .putString(KEY_SHARDS, writeShards(profile.shards))
            .putInt(KEY_TOTAL_SPINS, profile.totalSpins)
            .putInt(KEY_BEST_WIN, profile.bestWin)
            .putFloat(KEY_BEST_STREAK, profile.bestStreak)
            .putInt(KEY_TOP_UPS, profile.topUpsUsed)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun readIntMap(raw: String?): Map<Int, Int> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(ENTRY_SEPARATOR).mapNotNull { entry ->
            val parts = entry.split(PAIR_SEPARATOR)
            if (parts.size != 2) return@mapNotNull null
            val key = parts[0].toIntOrNull() ?: return@mapNotNull null
            val value = parts[1].toIntOrNull() ?: return@mapNotNull null
            key to value
        }.toMap()
    }

    private fun writeIntMap(map: Map<Int, Int>): String =
        map.entries.joinToString(ENTRY_SEPARATOR) { "${it.key}$PAIR_SEPARATOR${it.value}" }

    private fun readShards(raw: String?): Map<SlotSymbol, Int> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(ENTRY_SEPARATOR).mapNotNull { entry ->
            val parts = entry.split(PAIR_SEPARATOR)
            if (parts.size != 2) return@mapNotNull null
            val symbol = SlotSymbol.fromKey(parts[0]) ?: return@mapNotNull null
            val value = parts[1].toIntOrNull() ?: return@mapNotNull null
            symbol to value
        }.toMap()
    }

    private fun writeShards(map: Map<SlotSymbol, Int>): String =
        map.entries.joinToString(ENTRY_SEPARATOR) { "${it.key.key}$PAIR_SEPARATOR${it.value}" }

    private companion object {
        const val STORE = "club_profile"
        const val KEY_CHIPS = "chips"
        const val KEY_UNLOCKED = "unlocked_rooms"
        const val KEY_STARS = "stars"
        const val KEY_BEST_SCORES = "best_scores"
        const val KEY_SHARDS = "shards"
        const val KEY_TOTAL_SPINS = "total_spins"
        const val KEY_BEST_WIN = "best_win"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_TOP_UPS = "top_ups"
        const val ENTRY_SEPARATOR = ";"
        const val PAIR_SEPARATOR = ":"
    }
}
