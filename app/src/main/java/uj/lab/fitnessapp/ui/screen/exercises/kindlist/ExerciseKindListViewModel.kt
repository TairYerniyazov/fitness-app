package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun loadExercises() {
        viewModelScope.launch {
            val exercises = exerciseRepository.getAllExercises()
            _uiState.update {
                Log.d("DEBUG", "ExerciseListViewModel: ${exercises.size} exercises loaded")
                ExercisesUiState(exercises)
            }
        }
    }
}

data class ExercisesUiState(
    val exercises: List<Exercise>
)
