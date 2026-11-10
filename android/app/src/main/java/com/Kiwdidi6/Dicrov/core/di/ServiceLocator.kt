package com.Kiwdidi6.Dicrov.core.di

import android.content.Context
import com.Kiwdidi6.Dicrov.data.local.ClubPreferences
import com.Kiwdidi6.Dicrov.data.local.SettingsPreferences
import com.Kiwdidi6.Dicrov.data.repository.ClubRepositoryImpl
import com.Kiwdidi6.Dicrov.data.repository.SettingsRepositoryImpl
import com.Kiwdidi6.Dicrov.domain.repository.ClubRepository
import com.Kiwdidi6.Dicrov.domain.repository.SettingsRepository
import com.Kiwdidi6.Dicrov.domain.usecase.ApplyRoomResultUseCase
import com.Kiwdidi6.Dicrov.domain.usecase.BuildReelStripsUseCase
import com.Kiwdidi6.Dicrov.domain.usecase.EvaluatePayLinesUseCase
import com.Kiwdidi6.Dicrov.domain.usecase.SpinReelsUseCase

object ServiceLocator {

    private var clubRepositoryInternal: ClubRepository? = null
    private var settingsRepositoryInternal: SettingsRepository? = null

    val buildReelStrips: BuildReelStripsUseCase by lazy { BuildReelStripsUseCase() }
    val evaluatePayLines: EvaluatePayLinesUseCase by lazy { EvaluatePayLinesUseCase() }
    val spinReels: SpinReelsUseCase by lazy { SpinReelsUseCase(evaluatePayLines) }
    val applyRoomResult: ApplyRoomResultUseCase by lazy { ApplyRoomResultUseCase() }

    val clubRepository: ClubRepository
        get() = requireNotNull(clubRepositoryInternal) { MISSING_INIT }

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settingsRepositoryInternal) { MISSING_INIT }

    fun init(context: Context) {
        if (clubRepositoryInternal == null) {
            clubRepositoryInternal = ClubRepositoryImpl(ClubPreferences(context))
        }
        if (settingsRepositoryInternal == null) {
            settingsRepositoryInternal = SettingsRepositoryImpl(SettingsPreferences(context))
        }
    }

    private const val MISSING_INIT = "ServiceLocator.init was not called"
}
