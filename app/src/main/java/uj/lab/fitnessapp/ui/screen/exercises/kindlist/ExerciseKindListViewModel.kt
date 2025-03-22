package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uj.lab.fitnessapp.data.model.ExerciseKind
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject

@HiltViewModel
class ExerciseKindListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ExercisesUiState(
            exerciseRepository.getExerciseKinds()
        )
    )
    val uiState: StateFlow<ExercisesUiState> get() = _uiState
}

data class ExercisesUiState(
    val exerciseKinds: List<ExerciseKind>
)
