package com.Kiwdidi6.Dicrov.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kiwdidi6.Dicrov.core.di.AppViewModelFactory
import com.Kiwdidi6.Dicrov.core.navigation.Screen
import com.Kiwdidi6.Dicrov.core.theme.BgBase
import com.Kiwdidi6.Dicrov.presentation.collection.CollectionScreen
import com.Kiwdidi6.Dicrov.presentation.game.GameScreen
import com.Kiwdidi6.Dicrov.presentation.levelselect.LevelSelectScreen
import com.Kiwdidi6.Dicrov.presentation.menu.MenuScreen
import com.Kiwdidi6.Dicrov.presentation.progress.ProgressScreen
import com.Kiwdidi6.Dicrov.presentation.result.ResultScreen
import com.Kiwdidi6.Dicrov.presentation.settings.SettingsScreen
import com.Kiwdidi6.Dicrov.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(modifier = Modifier.fillMaxSize().background(BgBase)) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                if (targetState is Screen.Game) {
                    (slideInVertically(tween(360)) { height -> height / 6 } + fadeIn(tween(360)))
                        .togetherWith(fadeOut(tween(240)))
                } else {
                    fadeIn(tween(420)) togetherWith fadeOut(tween(300))
                }
            },
            label = "app_screen"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.onSplashFinished() }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = { roomIndex -> appViewModel.openRoom(roomIndex) },
                    onLevelSelect = { appViewModel.openRoute(Screen.LevelSelect) },
                    onCollection = { appViewModel.openRoute(Screen.Collection) },
                    onProgress = { appViewModel.openRoute(Screen.Progress) },
                    onSettings = { appViewModel.openRoute(Screen.Settings) }
                )

                is Screen.LevelSelect -> LevelSelectScreen(
                    onMenu = { appViewModel.toMenu() },
                    onEnterRoom = { roomIndex -> appViewModel.openRoom(roomIndex) }
                )

                is Screen.Game -> GameScreen(
                    roomIndex = current.roomIndex,
                    attempt = current.attempt,
                    onRoomFinished = { roomIndex -> appViewModel.showResult(roomIndex) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Result -> ResultScreen(
                    onPlayAgain = { roomIndex -> appViewModel.replayRoom(roomIndex) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Collection -> CollectionScreen(
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Progress -> ProgressScreen(
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.Settings -> SettingsScreen(
                    onMenu = { appViewModel.toMenu() }
                )
            }
        }
    }
}
