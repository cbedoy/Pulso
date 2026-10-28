package com.mx.weartwo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.wear.compose.material3.AppScaffold
import com.mx.weartwo.presentation.home.HomeScreen
import com.mx.weartwo.presentation.metronome.MetronomeScreen
import com.mx.weartwo.presentation.theme.WearTwoTheme

enum class Screen {
    Home,
    Metronome,
}

@Composable
fun WearApp() {
    WearTwoTheme {
        AppScaffold {
            var screen by rememberSaveable { mutableStateOf(Screen.Home) }
            when (screen) {
                Screen.Home -> HomeScreen(onMetronomeClick = { screen = Screen.Metronome })
                Screen.Metronome -> MetronomeScreen(onBack = { screen = Screen.Home })
            }
        }
    }
}
