package uj.lab.fitnessapp.ui.screen.exercises.kindlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject


@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState(emptyList(), emptyList()))
    val uiState: StateFlow<ExercisesUiState> get() = _uiState
    val filters = getFilters()
    private val _selectedFilters = MutableStateFlow<List<Filter>>(emptyList())
    val selectedFilters: StateFlow<List<Filter>> = _selectedFilters

    fun loadExercises() {
        viewModelScope.launch {
            val allExercises = exerciseRepository.getAllExercises()
            _uiState.update {
                ExercisesUiState(allExercises,allExercises)
            }
            filterExercises() // filter is preserved when navigating back
        }
    }
    fun filterExercises() {
        _uiState.value = _uiState.value.copy(
            filteredExercises = _uiState.value.allExercises.filter { exercise ->
                _selectedFilters.value.all { filter -> filter.predicate(exercise) }
            }
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
                filteredExercises = updatedExercises.filter { exercise ->
                    _selectedFilters.value.all { filter -> filter.predicate(exercise) }
                }
            )
        }
    }
    fun getSelectedFilters(): List<Filter> {
        return _selectedFilters.value
    }
    fun toggleFilter(filter: Filter) {
        val currentFilters = _selectedFilters.value.toMutableList()
        // can't select both cardio and strength
        if (filter.description == "Cardio") {
            currentFilters.removeIf { it.description == "Strength" }
        }
        if (filter.description == "Strength") {
            currentFilters.removeIf { it.description == "Cardio" }
        }

        if (currentFilters.contains(filter)) {
            Log.d("DEBUG", "toggleFilter: remove ${filter.description} ")
            currentFilters.remove(filter)
        }
        else {
            Log.d("DEBUG", "toggleFilter: add ${filter.description} ")
            currentFilters += filter
        }
        _selectedFilters.value = currentFilters
    }
}

data class ExercisesUiState(
    val allExercises: List<Exercise>,
    val filteredExercises: List<Exercise>
)
