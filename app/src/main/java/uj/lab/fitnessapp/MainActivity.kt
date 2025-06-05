package uj.lab.fitnessapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import uj.lab.fitnessapp.navigation.NavigationWrapper
import uj.lab.fitnessapp.ui.screen.settings.SettingsViewModel
import uj.lab.fitnessapp.ui.theme.FitnessAppTheme
import java.time.LocalDate
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: MainActivityViewModel by viewModels()

        installSplashScreen().apply{
            setKeepOnScreenCondition {
                !viewModel.uiState.value.isDatabaseInitialized
            }
        }
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val state by viewModel.uiState.collectAsState()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val isDarkTheme = settingsViewModel.isDarkTheme

            // set status bar style based on theme (dark/light)
            enableEdgeToEdge(
                statusBarStyle = if (isDarkTheme) {
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    )
                }
            )

            if (state.isDatabaseInitialized) {
                FitnessAppTheme(darkTheme = isDarkTheme) {
                    NavigationWrapper()
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}