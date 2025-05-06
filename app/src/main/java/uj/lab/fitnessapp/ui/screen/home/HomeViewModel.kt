package uj.lab.fitnessapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    fun loadExerciseInstances(workoutDate: String) {
        viewModelScope.launch {
            val exerciseInstances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsForDate(
                workoutDate
            )
            _uiState.update {
                HomeUiState(
                    exerciseInstances = exerciseInstances,
                    currentDate = workoutDate
                )
            }
        }
    }

    fun updateExerciseFavoriteStatus(exerciseName: String, isFavorite: Boolean) {
        _uiState.update { currentState ->
            val updatedInstances = currentState.exerciseInstances.map { instance ->
                if (instance.exercise?.exerciseName == exerciseName) {
                    val updatedExercise = instance.exercise!!.copy(isFavourite = isFavorite)
                    instance.copy(exercise = updatedExercise)
                } else {
                    instance
                }
            }
            currentState.copy(exerciseInstances = updatedInstances)
        }
    }

    fun deleteExerciseInstance(instanceId: Int) {
        viewModelScope.launch {
            exerciseInstanceRepository.deleteInstance(instanceId)

            val currentDate = _uiState.value.currentDate
            if (currentDate != null) {
                loadExerciseInstances(currentDate)
            }
        }
    }
}

data class HomeUiState(
    val exerciseInstances: List<ExerciseInstanceWithDetails> = emptyList(),
    val currentDate: String? = null
)