package uj.lab.fitnessapp.ui.screen.settings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme
    private val _themeText = mutableStateOf("Light Mode")
    val themeText: State<String> = _themeText
    private val _distanceUnit = MutableStateFlow(DistanceUnit("Kilometers", "metric"))
    val distanceUnit: StateFlow<DistanceUnit> = _distanceUnit.asStateFlow() // Use asStateFlow

    val distanceUnits = listOf(
        DistanceUnit("Kilometers", "metric"),
        DistanceUnit("Miles", "imperial")
    )

    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState.asStateFlow()

    fun toggleTheme(newValue: Boolean) {
        _isDarkTheme.value = newValue
        _themeText.value = if (newValue) "Dark Mode" else "Light Mode"
    }

    fun setDistanceUnit(unit: DistanceUnit) {
        _distanceUnit.update { unit }
        Log.d("SettingsViewModel", "Distance unit set to: ${unit.displayName}")
        // TODO: Save this preference (e.g., using DataStore)
    }
}

data class DistanceUnit(
    val displayName: String,
    val systemName: String
)

data class ExercisesUiState(
    val exercises: List<Exercise>
)