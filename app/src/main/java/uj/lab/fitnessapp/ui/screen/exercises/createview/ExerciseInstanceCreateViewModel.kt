package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
            _uiState.update {
                ExerciseInstanceCreateUiState(
                    exerciseId = exercise.id,
                    workoutType = exercise.workoutType
                )
            }
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

    fun saveExerciseInstance() {
        viewModelScope.launch {
            val state = _uiState.value
            val instanceId = exerciseInstanceRepository.insertInstance(
                ExerciseInstance(
                    id = 0,
                    exerciseID = state.exerciseId,
                    date = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE)
                )
            )
            state.workoutSets.forEach { workoutSet ->
                workoutSetRepository.insertWorkoutSet(
                    workoutSet.copy(instanceID = instanceId)
                )
            }
        }
    }
}

data class ExerciseInstanceCreateUiState(
    val exerciseId: Int = 0,
    val workoutType: WorkoutType = WorkoutType.Strength,
    val workoutSets: List<WorkoutSet> = emptyList(),
)