package uj.lab.fitnessapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import javax.inject.Inject


@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository
) : ViewModel() {
    // Przechowujemy listę punktów do wykresu
    private val _chartData = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val chartData: StateFlow<List<Pair<String, Int>>> = _chartData

    private val _exerciseID = MutableStateFlow(0)
    val exerciseID: StateFlow<Int> = _exerciseID

    fun getExerciseIDFromKind(exerciseKind: String) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseByName(exerciseKind)
            _exerciseID.value = exercise.id
        }
    }

    fun getAllInstancesByExerciseID(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceByExerciseID(exerciseID)
            // Przykład: chcemy wyciągnąć date i zsumować dystanse z wszystkich serii.
            val result = mutableListOf<Pair<String, Int>>()
            for (instance in allInstances) {
                // Pobierz szczegóły (w tym WorkoutSet)
                val details = exerciseInstanceRepository.getExerciseInstanceWithDetails(instance.id)
                val date = details.exerciseInstance?.date ?: "Unknown"

                // Zsumuj odległości (distance) ze wszystkich serii
                var totalDistance = 0
                details.seriesList?.forEach { set ->
                    totalDistance += (set.distance ?: 0)
                }

                // Dodaj do listy dane w formie (data, dystans)
                // Możesz oczywiście inaczej przetwarzać, np. budować mapę dat->wartości
                result.add(Pair(date, totalDistance))
            }
            // np. posortujmy daty, żeby się ładnie rysowały
            val sorted = result.sortedBy { it.first }
            _chartData.value = sorted
        }
    }
}
