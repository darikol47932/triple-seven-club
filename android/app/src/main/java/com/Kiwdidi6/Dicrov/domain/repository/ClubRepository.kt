package com.Kiwdidi6.Dicrov.domain.repository

import com.Kiwdidi6.Dicrov.domain.model.ClubProfile
import com.Kiwdidi6.Dicrov.domain.model.RoomResult
import kotlinx.coroutines.flow.StateFlow

interface ClubRepository {
    val profile: StateFlow<ClubProfile>
    val lastResult: StateFlow<RoomResult?>

    fun update(transform: (ClubProfile) -> ClubProfile)
    fun publishResult(result: RoomResult)
    fun reset()
}
