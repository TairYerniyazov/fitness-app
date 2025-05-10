package uj.lab.fitnessapp.ui.screen.analytics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExercisesUiState
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject

@HiltViewModel
class ExerciseToAnalyseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList(), emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState

    fun loadExercises() {
        viewModelScope.launch {
            val allExercises = exerciseRepository.getExercisesWithInstances()
            _uiState.update {
                ExercisesUiState(allExercises, allExercises)
            }
        }
    }

    fun filterExercises(text: String) {
        _uiState.value = _uiState.value.copy(
            filteredExercises = _uiState.value.allExercises.filter { exercise ->
                exercise.exerciseName.lowercase().contains(text.lowercase())
            }
        )
    }
}
