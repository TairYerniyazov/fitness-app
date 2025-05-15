package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import uj.lab.fitnessapp.data.utils.UnitConverter
import uj.lab.fitnessapp.ui.screen.settings.SettingsManager
import javax.inject.Inject

@HiltViewModel
class ExerciseInstanceCreateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
    private val workoutSetRepository: WorkoutSetRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseInstanceCreateUiState())
    val uiState: StateFlow<ExerciseInstanceCreateUiState> get() = _uiState

    private val _weightUnit = MutableStateFlow("kg")
    val weightUnit: StateFlow<String> = _weightUnit

    private val _distanceUnit = MutableStateFlow("m")
    val distanceUnit: StateFlow<String> = _distanceUnit

    private val _isImperialWeight = MutableStateFlow(false)
    val isImperialWeight: StateFlow<Boolean> = _isImperialWeight

    private val _isImperialDistance = MutableStateFlow(false)
    val isImperialDistance: StateFlow<Boolean> = _isImperialDistance

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _instanceId = MutableStateFlow<Int?>(null)

    private val _date = MutableStateFlow(0L)


    init {
        viewModelScope.launch {
            settingsManager.weightUnit.collect { unit ->
                _isImperialWeight.value = unit == "imperial"
                _weightUnit.value = if (unit == "imperial") "lbs" else "kg"
                refreshWorkoutSets()
            }
        }

        viewModelScope.launch {
            settingsManager.distanceUnit.collect { unit ->
                _isImperialDistance.value = unit == "imperial"
                _distanceUnit.value = if (unit == "imperial") "mi" else "km"
                refreshWorkoutSets()
            }
        }

        viewModelScope.launch {
            settingsManager.date.collect { date ->
                _date.value = date
            }
        }
    }

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
        val convertedSet = convertToMetric(workoutSet)
        _uiState.update { currentState ->
            val newWorkoutSets = currentState.workoutSets + convertedSet
            currentState.copy(workoutSets = newWorkoutSets)
        }
    }

    fun removeWorkoutSet(workoutSet: WorkoutSet) {
        _uiState.update { currentState ->
            val newWorkoutSets = currentState.workoutSets - workoutSet
            currentState.copy(workoutSets = newWorkoutSets)
        }
    }

    fun saveExerciseInstance(onSave: () -> Unit) {
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            if (_isEditMode.value && _instanceId.value != null) {
                val instanceId = _instanceId.value!!
                workoutSetRepository.deleteWorkoutSetsForInstance(instanceId)

                _uiState.value.workoutSets.forEach { workoutSet ->
                    workoutSetRepository.insertWorkoutSet(
                        workoutSet.copy(instanceID = instanceId)
                    )
                }
            } else {
                val state = _uiState.value
                val instanceId = exerciseInstanceRepository.insertInstance(
                    ExerciseInstance(
                        id = 0,
                        exerciseID = state.exerciseId,
                        date = _date.value
                    )
                )
                state.workoutSets.forEach { workoutSet ->
                    workoutSetRepository.insertWorkoutSet(
                        workoutSet.copy(instanceID = instanceId)
                    )
                }
            }
            _uiState.update { it.copy(isSaving = false) }
            onSave()
        }
    }

    fun loadExistingInstance(instanceId: Int) {
        viewModelScope.launch {
            _isEditMode.value = true
            _instanceId.value = instanceId

            val instance = exerciseInstanceRepository.getExerciseInstanceWithDetails(instanceId)
            instance.let {
                val exercise = exerciseRepository.getExerciseByName(it.exercise?.exerciseName ?: "")
                _uiState.update { currentState ->
                    currentState.copy(
                        exerciseId = exercise.id,
                        workoutType = exercise.workoutType,
                        workoutSets = it.seriesList?.toList() ?: emptyList()
                    )
                }
            }
        }
    }

    private fun convertToMetric(workoutSet: WorkoutSet): WorkoutSet {
        val metricLoad = if (workoutSet.load != null) {
            UnitConverter.storeWeight(workoutSet.load, _isImperialWeight.value)
        } else {
            workoutSet.load
        }

        val metricDistance = if (workoutSet.distance != null && workoutSet.distance > 0) {
            UnitConverter.storeDistance(workoutSet.distance.toDouble(), _isImperialDistance.value)
        } else {
            workoutSet.distance
        }

        return workoutSet.copy(
            load = metricLoad,
            distance = metricDistance
        )
    }

    private fun refreshWorkoutSets() {
        _uiState.update { it.copy() }
    }
}

data class ExerciseInstanceCreateUiState(
    val exerciseId: Int = 0,
    val workoutType: WorkoutType = WorkoutType.Strength,
    val workoutSets: List<WorkoutSet> = emptyList(),
    val isSaving: Boolean = false,
)