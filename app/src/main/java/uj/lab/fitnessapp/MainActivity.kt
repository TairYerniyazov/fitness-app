package uj.lab.fitnessapp

import android.os.Bundle
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
import uj.lab.fitnessapp.ui.theme.FitnessAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>();
            val state by viewModel.uiState.collectAsState();
            LaunchedEffect(Unit) {
                viewModel.populateDatabase()
            }

            if (state.isDatabaseInitialized) {
                FitnessAppTheme {
                    NavigationWrapper()
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}