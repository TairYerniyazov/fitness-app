package uj.lab.fitnessapp.ui.screen.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    var isDarkTheme = false

    var themeText = "Light Theme"
    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState

    fun loadExercises() {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAllExercises()
            _uiState.update {
                Log.d("DEBUG", "ExerciseListViewModel: ${exercises.size} exercises loaded")
                ExercisesUiState(exercises)
            }
        }
    }

    fun toggleTheme(newValue: Boolean) {
        isDarkTheme = newValue
    }
}

data class ExercisesUiState(
    val exercises: List<Exercise>
)
