package uj.lab.fitnessapp.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.ui.screen.settings.SettingsManager
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val exerciseInstances: List<ExerciseInstanceWithDetails> = emptyList(),
    val currentDate: Long? = null
)

@HiltViewModel
public class HomeViewModel @Inject constructor(
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
    private val workoutSetRepository: WorkoutSetRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    private val _date = MutableStateFlow(0L)
    val date: StateFlow<Long> = _date.asStateFlow()

    private val _isImperialWeight = MutableStateFlow(false)
    val isImperialWeight: StateFlow<Boolean> = _isImperialWeight.asStateFlow()

    private val _isImperialDistance = MutableStateFlow(false)
    val isImperialDistance: StateFlow<Boolean> = _isImperialDistance.asStateFlow()

    init {
        viewModelScope.launch {
            settingsManager.date.collect { date ->
                _date.value = date
            }
        }

        viewModelScope.launch {
            settingsManager.weightUnit.collect { unit ->
                _isImperialWeight.value = unit == "imperial"
            }
        }

        viewModelScope.launch {
            settingsManager.distanceUnit.collect { unit ->
                _isImperialDistance.value = unit == "imperial"
            }
        }
    }

    fun loadExerciseInstances(workoutDate: Long) {
        viewModelScope.launch {
            val exerciseInstances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsForDate(
                workoutDate
            )
            _uiState.update {
                HomeUiState(
                    exerciseInstances = ensureConsistentSetOrdering(exerciseInstances),
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

    fun setDate(date: Long) {
        _date.value = date
        viewModelScope.launch {
            settingsManager.setDate(date)
        }
    }

    fun updateWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            // Update the workout set in the database
            workoutSetRepository.updateWorkoutSet(workoutSet)

            // Refresh the exercise instances to reflect the changes
            refreshExerciseInstances()
        }
    }

    fun deleteWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            workoutSetRepository.deleteWorkoutSet(workoutSet.id)

            // Refresh the exercise instances to reflect the changes
            refreshExerciseInstances()
        }
    }

    /**
     * Ensures consistent ordering of workout sets in exercise instances:
     * keeps seriesList sorted by their original ID so item numbers never shift.
     */
    private fun ensureConsistentSetOrdering(instances: List<ExerciseInstanceWithDetails>): List<ExerciseInstanceWithDetails> {
        return instances.map { instance ->
            val sortedSeries = instance.seriesList
                // sort by the generated PK so newly added sets keep their number
                ?.sortedBy { it.id } 
                ?: emptyList()
            instance.copy(seriesList = sortedSeries)
        }
    }

    fun loadExerciseInstancesForDate(date: LocalDate) {
        viewModelScope.launch {
            val instances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsForDate(date.toEpochDay())
            _uiState.update {
                it.copy(exerciseInstances = ensureConsistentSetOrdering(instances))
            }
        }
    }

    private fun refreshExerciseInstances() {
        val currentDate = _date.value
        if (currentDate != 0L) {
            loadExerciseInstances(currentDate)
        }
    }
}