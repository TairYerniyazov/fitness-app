package uj.lab.fitnessapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.model.WorkoutType
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.mutableListOf

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseInstanceRepository: ExerciseInstanceRepository
) : ViewModel() {

    private val _chartData = MutableStateFlow<List<Pair<String, Any>>>(emptyList())
    val chartData: StateFlow<List<Pair<String, Any>>> = _chartData

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

    fun getInstancesInTimeRange(exerciseID: Int, startDate: LocalDate, endDate: LocalDate? = null){
        viewModelScope.launch {
            val _endDate = endDate ?: LocalDate.now()
            val startDateLong = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endDateLong = _endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val allInstances = exerciseInstanceRepository.getAllExerciseInstanceWithDetailsInRange(exerciseID, startDateLong, endDateLong)
            val exerciseType = allInstances.firstOrNull()?.exercise?.workoutType

            val result = computeStatistics(allInstances, exerciseType!!)
            _chartData.value = result.sortedBy { it.first }
        }
    }

    fun getAllTimeInstances(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceWithDetailsByExerciseId(exerciseID)
            var result = mutableListOf<Pair<String, Any>>()
            val exerciseType = allInstances.firstOrNull()?.exercise?.workoutType

            result = computeStatistics(allInstances, exerciseType!!)
            _chartData.value = result.sortedBy { it.first }
        }
    }

    fun computeStatistics(allInstances: List<ExerciseInstanceWithDetails>, exerciseType: WorkoutType):
        MutableList<Pair<String, Any>>
    {
        val result = mutableListOf<Pair<String, Any>>()

        for (instance in allInstances) {
            val dateMillis = instance.exerciseInstance?.date ?: 0L
            val dateTime = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            val date = formatter.format(dateTime)

            when (exerciseType) {
                WorkoutType.Cardio -> {
                    var totalDistance = 0
                    var totalTime = 0
                    instance.seriesList?.forEach { set ->
                        totalDistance += (set.distance ?: 0)
                        totalTime += (set.time ?: 0)
                    }
                    result.add(Pair(date, totalDistance))
                    result.add(Pair(date, totalTime))
                }
                WorkoutType.Strength -> {
                    var totalReps = 0
                    var trainingVolume = 0.0
                    instance.seriesList?.forEach { set ->
                        totalReps += (set.reps ?: 0)
                        trainingVolume += (set.reps ?: 0) * (set.load ?: 0.0)
                    }
                    result.add(Pair(date, totalReps))
                    result.add(Pair(date, trainingVolume))
                }
                null -> {}
            }
        }
        return result
    }

    // TODO: to też trzeba rozbić, no chyba, że chcemy bardziej ogólne info na górze Analityki
    fun calculateKeyMetrics(exerciseID: Int) {
        viewModelScope.launch {
            val allInstances = exerciseInstanceRepository.getExerciseInstanceByExerciseID(exerciseID)
            val metrics = mutableMapOf<String, Any>()

            for (instance in allInstances) {
                val details = exerciseInstanceRepository.getExerciseInstanceWithDetails(instance.id)
                val exerciseType = details.exercise?.workoutType

                when (exerciseType) {
                    WorkoutType.Cardio -> {
                        var totalDistance = 0
                        var totalTime = 0
                        var maxDistance = 0
                        var maxTime = 0
                        val sessionCount = allInstances.size

                        details.seriesList?.forEach { set ->
                            val dist = set.distance ?: 0
                            val time = set.time ?: 0
                            totalDistance += dist
                            totalTime += time
                            if (dist > maxDistance) maxDistance = dist
                            if (time > maxTime) maxTime = time
                        }

                        metrics["Całkowity dystans (m)"] = totalDistance
                        metrics["Całkowity czas (s)"] = totalTime
                        metrics["Maks. dystans (m)"] = maxDistance
                        metrics["Maks. czas (s)"] = maxTime
                        metrics["Liczba sesji"] = sessionCount
                    }

                    WorkoutType.Strength -> {
                        var totalReps = 0
                        var trainingVolume = 0.0
                        var estimated1RM = 0.0
                        var maxReps = 0
                        val sessionCount = allInstances.size

                        details.seriesList?.forEach { set ->
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

                        metrics["Całkowite powtórzenia"] = totalReps
                        metrics["Objętość treningu"] = String.format("%.1f", trainingVolume)
                        metrics["Szacowany 1RM"] = String.format("%.1f", estimated1RM)
                        metrics["Maks. liczba powtórzeń"] = maxReps
                        metrics["Liczba sesji"] = sessionCount
                    }

                    null -> {}
                }
            }

            _metricsData.value = metrics
        }
    }
}
