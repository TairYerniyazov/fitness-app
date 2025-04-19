package uj.lab.fitnessapp.ui.screen.settings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    var isDarkTheme by mutableStateOf(false)
        private set


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

    init {
        viewModelScope.launch {
            settingsManager.isDarkThemeEnabled.collect { isDark ->
                isDarkTheme = isDark
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            settingsManager.setDarkThemeEnabled(!isDarkTheme)
        }
    }

    fun setDistanceUnit(unit: MetricUnit) {
        _distanceUnit.value = unit
        viewModelScope.launch {
            settingsManager.setDistanceUnit(unit.systemName)
        }
        Log.d("SettingsViewModel", "Distance unit set to: ${unit.displayName}")
    }

    fun setWeightUnit(unit: MetricUnit) {
        _weightUnit.value = unit
        viewModelScope.launch {
            settingsManager.setWeightUnit(unit.systemName)
        }
        Log.d("SettingsViewModel", "Weight unit set to: ${unit.displayName}")
    }
}

data class MetricUnit(
    val displayName: String,
    val systemName: String
)

data class ExercisesUiState(
    val exercises: List<Exercise>
)