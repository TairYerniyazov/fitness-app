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
    private val _themeText = mutableStateOf("Tryb jasny")
    val themeText: State<String> = _themeText

    val distanceUnits = listOf(
        MetricUnit("Kilometry (km)", "metric"),
        MetricUnit("Mile (mi)", "imperial")
    )
    private val _distanceUnit = MutableStateFlow(distanceUnits[0])
    val distanceUnit: StateFlow<MetricUnit> = _distanceUnit.asStateFlow()

    val weightUnits = listOf(
        MetricUnit("Kilogramy (kg)", "metric"),
        MetricUnit("Funty (lb)", "imperial")
    )
    private val _weightUnit = MutableStateFlow(weightUnits[0])
    val weightUnit: StateFlow<MetricUnit> = _weightUnit.asStateFlow()


    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState.asStateFlow()

    fun toggleTheme(newValue: Boolean) {
        _isDarkTheme.value = newValue
        _themeText.value = if (newValue) "Tryb ciemny" else "Tryb jasny"
    }

    fun setDistanceUnit(unit: MetricUnit) {
        _distanceUnit.update { unit }
        Log.d("SettingsViewModel", "Distance unit set to: ${unit.displayName}")
        // TODO: Save this preference (e.g., using DataStore)
    }

    fun setWeightUnit(unit: MetricUnit) {
        _weightUnit.update { unit }
        Log.d("SettingsViewModel", "Distance unit set to: ${unit.displayName}")
        // TODO: Save this preference (e.g., using DataStore)
    }
}

data class MetricUnit(
    val displayName: String,
    val systemName: String
)

data class ExercisesUiState(
    val exercises: List<Exercise>
)