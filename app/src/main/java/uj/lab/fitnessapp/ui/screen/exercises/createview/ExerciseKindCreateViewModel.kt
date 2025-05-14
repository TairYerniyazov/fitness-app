package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.ui.screen.settings.SettingsManager
import javax.inject.Inject

@HiltViewModel
class ExerciseKindCreateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseKindCreateUiState())
    val uiState: StateFlow<ExerciseKindCreateUiState> get() = _uiState

    fun saveNewExercise() {
    }

    fun setNewExerciseName(it: String) {
        _uiState.value = _uiState.value.copy(exerciseName = it)
    }

    fun setNewExerciseType(it: WorkoutType) {
        _uiState.value = _uiState.value.copy(exerciseType = it)
    }

    fun setNewExerciseFavorite(it: Boolean) {
        _uiState.value = _uiState.value.copy(isFavorite = it)
    }

    fun setOptionsFromSelectedFilters() {
        
    }
}

data class ExerciseKindCreateUiState(
    val exerciseName: String = "New Exercise",
    val exerciseType: WorkoutType = WorkoutType.Strength,
    val isFavorite: Boolean = false
)