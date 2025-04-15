package uj.lab.fitnessapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import uj.lab.fitnessapp.ui.screen.exercises.createview.ExerciseInstanceCreateUiState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    fun loadExerciseInstances() {
        viewModelScope.launch {
            val exerciseInstances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsForDate(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE)
            )
            _uiState.update {
                HomeUiState(
                    exerciseInstances = exerciseInstances
                )
            }
        }
    }
}

data class HomeUiState(
    val exerciseInstances: List<ExerciseInstanceWithDetails> = emptyList(),
)