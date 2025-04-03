package uj.lab.fitnessapp.ui.screen.exercises.createview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.ui.screen.exercises.kindlist.ExercisesUiState
import javax.inject.Inject

@HiltViewModel
internal class ExerciseInstanceCreateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseInstanceCreateUiState())
    val uiState: StateFlow<ExerciseInstanceCreateUiState> get() = _uiState

    init {
        loadExercise()
    }

    private fun loadExercise() {
//        viewModelScope.launch {
//            val exercises = exerciseRepository.getExerciseByName()
//            _uiState.value = ExercisesUiState(exercises)
//        }
    }
}

data class ExerciseInstanceCreateUiState(
    var exercise: ExerciseInstanceWithDetails? = null,
)