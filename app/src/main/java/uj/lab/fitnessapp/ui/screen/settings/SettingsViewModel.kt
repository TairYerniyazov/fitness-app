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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
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

    init {
        viewModelScope.launch {
            settingsManager.distanceUnit.collect { systemName ->
                _distanceUnit.value =
                    distanceUnits.find { it.systemName == systemName } ?: distanceUnits[0]
            }
        }

        viewModelScope.launch {
            settingsManager.weightUnit.collect { systemName ->
                _weightUnit.value =
                    weightUnits.find { it.systemName == systemName } ?: weightUnits[0]
            }
        }

        viewModelScope.launch {
            settingsManager.isDarkTheme.collect { isDark ->
                _isDarkTheme.value = isDark
                _themeText.value = if (isDark) "Tryb ciemny" else "Tryb jasny"
            }
        }
    }

    fun toggleTheme(newValue: Boolean) {
        _isDarkTheme.value = newValue
        _themeText.value = if (newValue) "Tryb ciemny" else "Tryb jasny"
        viewModelScope.launch {
            settingsManager.toggleTheme(newValue)
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