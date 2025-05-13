package uj.lab.fitnessapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject


@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository
) : ViewModel() {
    private val _exerciseID = MutableStateFlow(0)
    val exerciseID: StateFlow<Int> = _exerciseID

    fun getExerciseIDFromKind(exerciseKind: String) {
        viewModelScope.launch {
            val exercise  = exerciseRepository.getExerciseByName(exerciseKind)
            _exerciseID.update { exercise.id }
        }
    }

    fun getAllInstancesByExerciseID(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceByExerciseID(exerciseID)
            for (e in allInstances) {
                println(exerciseInstanceRepository.getExerciseInstanceWithDetails(e.id))
            }
        }
    }
    // TODO: add analytics logic
}
