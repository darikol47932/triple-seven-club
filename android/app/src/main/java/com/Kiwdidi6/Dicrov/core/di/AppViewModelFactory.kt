package com.Kiwdidi6.Dicrov.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kiwdidi6.Dicrov.presentation.app.AppViewModel
import com.Kiwdidi6.Dicrov.presentation.collection.CollectionViewModel
import com.Kiwdidi6.Dicrov.presentation.game.GameViewModel
import com.Kiwdidi6.Dicrov.presentation.levelselect.LevelSelectViewModel
import com.Kiwdidi6.Dicrov.presentation.menu.MenuViewModel
import com.Kiwdidi6.Dicrov.presentation.progress.ProgressViewModel
import com.Kiwdidi6.Dicrov.presentation.result.ResultViewModel
import com.Kiwdidi6.Dicrov.presentation.settings.SettingsViewModel
import com.Kiwdidi6.Dicrov.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val club = ServiceLocator.clubRepository
        val settings = ServiceLocator.settingsRepository
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(club)
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
                LevelSelectViewModel(club)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                club = club,
                settingsRepository = settings,
                buildReelStrips = ServiceLocator.buildReelStrips,
                spinReels = ServiceLocator.spinReels,
                applyRoomResult = ServiceLocator.applyRoomResult
            )
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> ResultViewModel(club)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(club)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(club)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settings, club)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
