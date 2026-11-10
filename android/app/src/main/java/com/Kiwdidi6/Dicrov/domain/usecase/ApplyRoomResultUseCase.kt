package com.Kiwdidi6.Dicrov.domain.usecase

import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.RoomOutcome
import com.Kiwdidi6.Dicrov.domain.model.RoomResult
import kotlin.math.max

class ApplyRoomResultUseCase {

    operator fun invoke(profile: ClubProfile, result: RoomResult, totalRooms: Int): ClubProfile {
        val previousStars = profile.stars[result.roomIndex] ?: 0
        val stars = profile.stars.toMutableMap()
        stars[result.roomIndex] = max(previousStars, result.stars)

        val bestScores = profile.bestScores.toMutableMap()
        bestScores[result.roomIndex] = max(profile.bestScores[result.roomIndex] ?: 0, result.points)

        val unlocked = if (result.outcome == RoomOutcome.CLEARED) {
            max(profile.unlockedRooms, (result.roomIndex + 2).coerceAtMost(totalRooms))
        } else {
            profile.unlockedRooms
        }

        return profile.copy(
            stars = stars,
            bestScores = bestScores,
            unlockedRooms = unlocked,
            bestStreak = max(profile.bestStreak, result.bestStreak)
        )
    }

    fun starsFor(points: Int, target: Int): Int {
        if (target <= 0) return 0
        val ratio = points.toFloat() / target.toFloat()
        return when {
            ratio >= 1.4f -> 3
            ratio >= 1.0f -> 2
            ratio >= 0.6f -> 1
            else -> 0
        }
    }
}
