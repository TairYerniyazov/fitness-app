package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExercisesUiState
import javax.inject.Inject

@HiltViewModel
internal class ExerciseInstanceCreateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
    private val workoutSetRepository: WorkoutSetRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseInstanceCreateUiState())
    val uiState: StateFlow<ExerciseInstanceCreateUiState> get() = _uiState

    fun loadExercise(name: String) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseByName(name)
            _uiState.update { ExerciseInstanceCreateUiState(workoutType = exercise.workoutType) }
        }
    }

    fun addWorkoutSet(workoutSet: WorkoutSet) {
        _uiState.update { currentState ->
            val newWorkoutSets = currentState.workoutSets + workoutSet
            currentState.copy(workoutSets = newWorkoutSets)
        }
    }

    fun removeWorkoutSet(workoutSet: WorkoutSet) {
        _uiState.update { currentState ->
            val newWorkoutSets = currentState.workoutSets - workoutSet
            currentState.copy(workoutSets = newWorkoutSets)
        }
    }
}

data class ExerciseInstanceCreateUiState(
    val workoutType: WorkoutType = WorkoutType.Strength,
    val workoutSets: List<WorkoutSet> = emptyList(),
)