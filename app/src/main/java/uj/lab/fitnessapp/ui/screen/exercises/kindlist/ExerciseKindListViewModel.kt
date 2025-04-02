package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject


@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAllExercises()
            _uiState.value = ExercisesUiState(exercises)
        }
    }
}

data class ExercisesUiState(
    val exercises: List<Exercise>
)
