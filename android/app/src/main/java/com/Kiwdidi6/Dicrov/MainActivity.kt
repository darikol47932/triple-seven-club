package com.Kiwdidi6.Dicrov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Kiwdidi6.Dicrov.core.di.ServiceLocator
import com.Kiwdidi6.Dicrov.core.theme.AppTheme
import com.Kiwdidi6.Dicrov.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
