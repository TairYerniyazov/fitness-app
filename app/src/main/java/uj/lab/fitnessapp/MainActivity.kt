package uj.lab.fitnessapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val state by viewModel.uiState.collectAsState()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val isDarkTheme = settingsViewModel.isDarkTheme

            LaunchedEffect(Unit) {
                viewModel.populateDatabase()

                val currentDate = LocalDate.now()
                val dateMillis = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
//                Log.d("Date", "date: ${dateMillis}")
                settingsViewModel.setDate(dateMillis)
            }

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