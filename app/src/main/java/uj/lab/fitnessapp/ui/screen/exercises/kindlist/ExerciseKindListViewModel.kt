package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.compose.runtime.Composable
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
//import uj.lab.fitnessapp.ui.screen.exercises.kindlist.getFilters
import javax.inject.Inject


@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList(), emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState
    private val filters = getFilters()
    private var currentFilter = filters.filter{ it.description == "All" }[0]

    fun loadExercises() {
        viewModelScope.launch {
            val allExercises = exerciseRepository.getAllExercises()
            _uiState.update {
                Log.d("DEBUG", "ExerciseListViewModel: ${allExercises.size} exercises loaded")
                ExercisesUiState(allExercises,allExercises)
            }
            filterExercises(currentFilter) // filter is preserved when navigating back
        }
    }
    fun filterExercises(filter: Filter) {
        _uiState.value = _uiState.value.copy(
            filteredExercises = _uiState.value.allExercises.filter(filter.predicate)
        )
    }
    fun toggleFavorite(exercise: Exercise) {
        viewModelScope.launch {
            val updatedExercise = exercise.copy(isFavourite = !exercise.isFavourite)
            exerciseRepository.updateExercise(updatedExercise)

            val updatedExercises  = _uiState.value.allExercises.map {
                if (it.exerciseName == updatedExercise.exerciseName) updatedExercise else it
            }


            _uiState.value = _uiState.value.copy(
                allExercises = updatedExercises,
                filteredExercises = updatedExercises.filter(currentFilter.predicate)
            )
        }
    }
    fun setSelectedFilter(filter: Filter) {
        currentFilter = filter
    }
    fun getSelectedFilter(): Filter {
        return currentFilter
    }
}

data class ExercisesUiState(
    val allExercises: List<Exercise>,
    val filteredExercises: List<Exercise>
)
