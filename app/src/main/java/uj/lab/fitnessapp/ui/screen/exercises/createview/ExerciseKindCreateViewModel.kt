package uj.lab.fitnessapp.ui.screen.exercises.createview

import android.R.attr.name
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
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
        viewModelScope.launch {
            val newExercise = Exercise(
                id = 0,
                exerciseName = _uiState.value.exerciseName,
                workoutType = _uiState.value.workoutType,
                canModify = true,
                isFavourite = _uiState.value.isFavorite
            )
            exerciseRepository.insertExercise(newExercise)
        }
    }

    fun updateExercise(kindId: Int) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(Exercise(
                id = kindId,
                exerciseName = _uiState.value.exerciseName,
                workoutType = _uiState.value.workoutType,
                canModify = true,
                isFavourite = _uiState.value.isFavorite
            ))
        }
    }

    fun loadExerciseKind(kindId: Int) {
        viewModelScope.launch {
            val kind = exerciseRepository.getExerciseById(kindId)
            _uiState.update {
                it.copy(
                    exerciseName = kind.exerciseName,
                    workoutType = kind.workoutType,
                    isFavorite = kind.isFavourite
                )
            }
        }
    }

    fun setExerciseName(name: String) {
        _uiState.update {
            it.copy(
                exerciseName = name,
            )
        }
    }

    fun setExerciseType(ty: WorkoutType) {
        _uiState.update {
            it.copy(
                workoutType = ty,
            )
        }
    }

    fun setExerciseFavorite(isFavorite: Boolean) {
        _uiState.update {
            it.copy(
                isFavorite = isFavorite,
            )
        }
    }
}

data class ExerciseKindCreateUiState(
    val exerciseName: String = "New Exercise",
    val workoutType: WorkoutType = WorkoutType.Strength,
    val isFavorite: Boolean = false
)