package uj.lab.fitnessapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.ui.screen.settings.SettingsManager
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CardioChartData(
    val date: String,
    val distance: Double,
    val time: Double,
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
    private val exerciseInstanceRepository: ExerciseInstanceRepository,
    private val settingsManager: SettingsManager
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

    private val _currentDistanceUnit = MutableStateFlow("metric")
    val currentDistanceUnit: StateFlow<String> = _currentDistanceUnit

    private val _currentWeightUnit = MutableStateFlow("metric")
    val currentWeightUnit: StateFlow<String> = _currentWeightUnit

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    init {
        viewModelScope.launch {
            settingsManager.distanceUnit.collect { unit ->
                _currentDistanceUnit.value = unit
                if (_exerciseID.value > 0) {
                    getAllTimeInstances(_exerciseID.value)
                }
            }
        }
        viewModelScope.launch {
            settingsManager.weightUnit.collect { unit ->
                _currentWeightUnit.value = unit
                if (_exerciseID.value > 0 && _exerciseType.value == WorkoutType.Strength) {
                    getAllTimeInstances(_exerciseID.value)
                }
            }
        }
    }

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

    fun getInstancesInTimeRangeLong(exerciseID: Int, startDateLong: Long, endDateLong: Long? = null) {
        viewModelScope.launch {
            val endDateLong = endDateLong ?: LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
            calculateKeyMetrics(exerciseID)
        }
    }

    private fun computeCardioStatistics(allInstances: List<ExerciseInstanceWithDetails>): List<CardioChartData> {
        val isImperial = _currentDistanceUnit.value == "imperial"
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

            val totalDistanceConverted = if (isImperial) totalDistanceMeters * 0.000621371 else totalDistanceMeters / 1000.0
            val totalTimeHours = totalTimeSeconds / 3600.0
            val velocityConverted = if (totalTimeHours > 0) totalDistanceConverted / totalTimeHours else 0.0

            CardioChartData(date, totalDistanceConverted, totalTimeHours, velocityConverted)
        }
    }

    private fun computeStrengthStatistics(allInstances: List<ExerciseInstanceWithDetails>): List<StrengthChartData> {
        val isImperial = _currentWeightUnit.value == "imperial"
        return allInstances.map { instance ->
            val dateMillis = instance.exerciseInstance?.date ?: 0L
            val dateTime = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            val date = formatter.format(dateTime)

            var totalReps = 0
            var totalLoadKg = 0.0
            var trainingVolume = 0.0
            var estimated1RM = 0.0

            instance.seriesList?.forEach { set ->
                val reps = set.reps ?: 0
                val loadKg = set.load ?: 0.0
                totalReps += reps
                totalLoadKg += loadKg

                val currentLoadConverted = if (isImperial) loadKg * 2.20462 else loadKg
                trainingVolume += reps * currentLoadConverted

                if (reps > 0) {
                    val oneRM = currentLoadConverted * (1 + reps / 30.0)
                    if (oneRM > estimated1RM) estimated1RM = oneRM
                }
            }

            StrengthChartData(
                date = date,
                reps = totalReps,
                load = if (instance.seriesList.isNullOrEmpty()) 0.0 else (totalLoadKg / instance.seriesList.size).let {
                    if (isImperial) it * 2.20462 else it
                },
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

            val isDistanceImperial = _currentDistanceUnit.value == "imperial"
            val isWeightImperial = _currentWeightUnit.value == "imperial"

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

                    val totalDistanceConverted = if (isDistanceImperial) totalDistanceMeters * 0.000621371 else totalDistanceMeters / 1000.0
                    val maxDistanceConverted = if (isDistanceImperial) maxDistanceMeters * 0.000621371 else maxDistanceMeters / 1000.0
                    val totalTimeHours = totalTimeSeconds / 3600.0
                    val maxTimeHours = maxTimeSeconds / 3600.0
                    val avgVelocityConverted = if (isDistanceImperial) (totalVelocityMps / sessionCount) * 2.23694 else (totalVelocityMps / sessionCount) * 3.6
                    val maxVelocityConverted = if (isDistanceImperial) maxVelocityMps * 2.23694 else maxVelocityMps * 3.6

                    metrics["Całkowity dystans (${if (isDistanceImperial) "mi" else "km"})"] = String.format("%.2f", totalDistanceConverted)
                    metrics["Całkowity czas (h)"] = String.format("%.2f", totalTimeHours)
                    metrics["Maks. dystans (${if (isDistanceImperial) "mi" else "km"})"] = String.format("%.2f", maxDistanceConverted)
                    metrics["Maks. czas (h)"] = String.format("%.2f", maxTimeHours)
                    metrics["Liczba sesji"] = sessionCount
                    metrics["Średnia prędkość (${if (isDistanceImperial) "mi/h" else "km/h"})"] = String.format("%.2f", avgVelocityConverted)
                    metrics["Maks. prędkość (${if (isDistanceImperial) "mi/h" else "km/h"})"] = String.format("%.2f", maxVelocityConverted)
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
                            val loadKg = set.load ?: 0.0
                            totalReps += reps

                            val currentLoadConverted = if (isWeightImperial) loadKg * 2.20462 else loadKg
                            trainingVolume += reps * currentLoadConverted

                            if (reps > 0) {
                                val oneRM = currentLoadConverted * (1 + reps / 30.0)
                                if (oneRM > estimated1RM) estimated1RM = oneRM
                            }
                            if (reps > maxReps) maxReps = reps
                        }
                    }

                    metrics["Całkowite powtórzenia"] = totalReps
                    metrics["Objętość treningu (${if (isWeightImperial) "lb" else "kg"})"] = String.format("%.1f", trainingVolume)
                    metrics["Szacowany 1RM (${if (isWeightImperial) "lb" else "kg"})"] = String.format("%.1f", estimated1RM)
                    metrics["Maks. liczba powtórzeń"] = maxReps
                    metrics["Liczba sesji"] = sessionCount
                }
                null -> {}
            }

            _metricsData.value = metrics
        }
    }
}