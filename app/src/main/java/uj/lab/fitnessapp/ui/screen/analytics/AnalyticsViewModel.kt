package uj.lab.fitnessapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CardioChartData(
    val date: String,
    val distance: Double, // Zmieniono na Double, żeby obsłużyć ułamki po konwersji
    val time: Double,    // Zmieniono na Double, żeby obsłużyć ułamki po konwersji
    val velocity: Double
)

data class StrengthChartData(
    val date: String,
    val reps: Int,
    val load: Double,
    val volume: Double,
    val estimated1RM: Double
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository
) : ViewModel() {

    private val _cardioChartData = MutableStateFlow<List<CardioChartData>>(emptyList())
    val cardioChartData: StateFlow<List<CardioChartData>> = _cardioChartData

    private val _strengthChartData = MutableStateFlow<List<StrengthChartData>>(emptyList())
    val strengthChartData: StateFlow<List<StrengthChartData>> = _strengthChartData

    private val _exerciseID = MutableStateFlow(0)
    val exerciseID: StateFlow<Int> = _exerciseID

    private val _exerciseName = MutableStateFlow("")
    val exerciseName: StateFlow<String> = _exerciseName

    private val _exerciseType = MutableStateFlow<WorkoutType?>(null)
    val exerciseType: StateFlow<WorkoutType?> = _exerciseType

    private val _metricsData = MutableStateFlow<Map<String, Any>>(emptyMap())
    val metricsData: StateFlow<Map<String, Any>> = _metricsData

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun getExerciseIDFromKind(exerciseKind: String) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseByName(exerciseKind)
            _exerciseID.value = exercise.id
            _exerciseName.value = exercise.exerciseName
            _exerciseType.value = exercise.workoutType
            calculateKeyMetrics(exercise.id)
        }
    }

    fun getInstancesInTimeRange(exerciseID: Int, startDate: LocalDate, endDate: LocalDate? = null) {
        viewModelScope.launch {
            val _endDate = endDate ?: LocalDate.now()
            val startDateLong = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endDateLong = _endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val allInstances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsInRange(exerciseID, startDateLong, endDateLong)

            val exerciseType = allInstances.firstOrNull()?.exercise?.workoutType
            when (exerciseType) {
                WorkoutType.Cardio -> _cardioChartData.value = computeCardioStatistics(allInstances)
                WorkoutType.Strength -> _strengthChartData.value = computeStrengthStatistics(allInstances)
                null -> {}
            }
        }
    }

    fun getAllTimeInstances(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceWithDetailsByExerciseId(exerciseID)
            val exerciseType = allInstances.firstOrNull()?.exercise?.workoutType

            when (exerciseType) {
                WorkoutType.Cardio -> _cardioChartData.value = computeCardioStatistics(allInstances)
                WorkoutType.Strength -> _strengthChartData.value = computeStrengthStatistics(allInstances)
                null -> {}
            }
        }
    }

    private fun computeCardioStatistics(allInstances: List<ExerciseInstanceWithDetails>): List<CardioChartData> {
        return allInstances.map { instance ->
            val dateMillis = instance.exerciseInstance?.date ?: 0L
            val dateTime = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            val date = formatter.format(dateTime)

            var totalDistanceMeters = 0.0
            var totalTimeSeconds = 0

            instance.seriesList?.forEach { set ->
                totalDistanceMeters += (set.distance ?: 0).toDouble()
                totalTimeSeconds += (set.time ?: 0)
            }

            // Konwersja na kilometr i godziny
            val totalDistanceKm = totalDistanceMeters / 1000.0
            val totalTimeHours = totalTimeSeconds / 3600.0
            val velocityKmH = if (totalTimeHours > 0) totalDistanceKm / totalTimeHours else 0.0

            CardioChartData(date, totalDistanceKm, totalTimeHours, velocityKmH)
        }
    }

    private fun computeStrengthStatistics(allInstances: List<ExerciseInstanceWithDetails>): List<StrengthChartData> {
        return allInstances.map { instance ->
            val dateMillis = instance.exerciseInstance?.date ?: 0L
            val dateTime = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            val date = formatter.format(dateTime)

            var totalReps = 0
            var totalLoad = 0.0
            var trainingVolume = 0.0
            var estimated1RM = 0.0

            instance.seriesList?.forEach { set ->
                val reps = set.reps ?: 0
                val load = set.load ?: 0.0
                totalReps += reps
                totalLoad += load
                trainingVolume += reps * load

                if (reps > 0) {
                    val oneRM = load * (1 + reps / 30.0)
                    if (oneRM > estimated1RM) estimated1RM = oneRM
                }
            }

            StrengthChartData(
                date = date,
                reps = totalReps,
                load = if (instance.seriesList.isNullOrEmpty()) 0.0 else totalLoad / instance.seriesList.size,
                volume = trainingVolume,
                estimated1RM = estimated1RM
            )
        }
    }

    fun calculateKeyMetrics(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceWithDetailsByExerciseId(exerciseID)
            val exerciseType = allInstances.firstOrNull()?.exercise?.workoutType
            val metrics = mutableMapOf<String, Any>()

            when (exerciseType) {
                WorkoutType.Cardio -> {
                    var totalDistanceMeters = 0.0
                    var totalTimeSeconds = 0
                    var maxDistanceMeters = 0.0
                    var maxTimeSeconds = 0
                    var totalVelocityMps = 0.0
                    var maxVelocityMps = 0.0
                    val sessionCount = allInstances.size

                    allInstances.forEach { instance ->
                        instance.seriesList?.forEach { set ->
                            val dist = (set.distance ?: 0).toDouble()
                            val time = set.time ?: 0
                            totalDistanceMeters += dist
                            totalTimeSeconds += time
                            if (dist > maxDistanceMeters) maxDistanceMeters = dist
                            if (time > maxTimeSeconds) maxTimeSeconds = time

                            val currentVelocity = if (time > 0) dist / time else 0.0
                            totalVelocityMps += currentVelocity
                            if (currentVelocity > maxVelocityMps) maxVelocityMps = currentVelocity
                        }
                    }

                    // Konwersja na kilometry, godziny i km/h
                    metrics["Całkowity dystans (km)"] = String.format("%.2f", totalDistanceMeters / 1000.0)
                    metrics["Całkowity czas (h)"] = String.format("%.2f", totalTimeSeconds / 3600.0)
                    metrics["Maks. dystans (km)"] = String.format("%.2f", maxDistanceMeters / 1000.0)
                    metrics["Maks. czas (h)"] = String.format("%.2f", maxTimeSeconds / 3600.0)
                    metrics["Liczba sesji"] = sessionCount
                    metrics["Średnia prędkość (km/h)"] = String.format("%.2f", if (sessionCount > 0) (totalVelocityMps / sessionCount) * 3.6 else 0.0) // m/s na km/h
                    metrics["Maks. prędkość (km/h)"] = String.format("%.2f", maxVelocityMps * 3.6) // m/s na km/h
                }

                WorkoutType.Strength -> {
                    var totalReps = 0
                    var trainingVolume = 0.0
                    var estimated1RM = 0.0
                    var maxReps = 0
                    val sessionCount = allInstances.size

                    allInstances.forEach { instance ->
                        instance.seriesList?.forEach { set ->
                            val reps = set.reps ?: 0
                            val load = set.load ?: 0.0
                            totalReps += reps
                            trainingVolume += reps * load
                            if (reps > 0) {
                                val oneRM = load * (1 + reps / 30.0)
                                if (oneRM > estimated1RM) estimated1RM = oneRM
                            }
                            if (reps > maxReps) maxReps = reps
                        }
                    }

                    metrics["Całkowite powtórzenia"] = totalReps
                    metrics["Objętość treningu"] = String.format("%.1f", trainingVolume)
                    metrics["Szacowany 1RM"] = String.format("%.1f", estimated1RM)
                    metrics["Maks. liczba powtórzeń"] = maxReps
                    metrics["Liczba sesji"] = sessionCount
                }
                null -> {}
            }

            _metricsData.value = metrics
        }
    }
}