package com.Kiwdidi6.Dicrov.presentation.app

import androidx.lifecycle.ViewModel
import com.Kiwdidi6.Dicrov.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()
    private var attempts = 0

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun onSplashFinished() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun openRoute(target: Screen) {
        backStack.addLast(_screen.value)
        _screen.value = target
        syncBack()
    }

    fun openRoom(roomIndex: Int) {
        attempts += 1
        backStack.addLast(_screen.value)
        _screen.value = Screen.Game(roomIndex, attempts)
        syncBack()
    }

    fun replayRoom(roomIndex: Int) {
        attempts += 1
        _screen.value = Screen.Game(roomIndex, attempts)
        syncBack()
    }

    fun showResult(roomIndex: Int) {
        _screen.value = Screen.Result(roomIndex)
        syncBack()
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
