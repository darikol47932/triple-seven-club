package com.Kiwdidi6.Dicrov.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelSelect : Screen
    data class Game(val roomIndex: Int, val attempt: Int) : Screen
    data class Result(val roomIndex: Int) : Screen
    data object Collection : Screen
    data object Progress : Screen
    data object Settings : Screen
}
