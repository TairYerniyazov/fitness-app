package uj.lab.fitnessapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.source.AppDatabase
import uj.lab.fitnessapp.navigation.NavigationWrapper
import uj.lab.fitnessapp.ui.theme.FitnessAppTheme
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val db: AppDatabase,
    private val application: Application,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> get() = _uiState

    suspend fun populateDatabase() {
        if (!uiState.value.isDatabaseInitialized) {
            db.populateDatabase(application.applicationContext)
            _uiState.update {
                it.copy(isDatabaseInitialized = true)
            }
        }
    }
}

data class MainActivityUiState(
    val isDatabaseInitialized: Boolean = false,
)