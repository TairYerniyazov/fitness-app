package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails


interface ExerciseInstanceRepository {

    suspend fun insertInstance(instance: ExerciseInstance): Int
    suspend fun updateInstance(instance: ExerciseInstance)
    suspend fun deleteInstance(exerciseID: Int)
    suspend fun getExerciseInstanceByExerciseID(exerciseID: Int): List<ExerciseInstance>
    suspend fun getExerciseInstanceWithDetails(instanceID: Int): ExerciseInstanceWithDetails
    suspend fun getExerciseInstanceWithDetailsByExerciseId(exerciseID: Int): List<ExerciseInstanceWithDetails>
    suspend fun getAllExerciseInstanceWithDetailsForDate(date: Long): List<ExerciseInstanceWithDetails>
    suspend fun getAllExerciseInstanceWithDetailsInRange(exerciseID: Int, startDate: Long, endDate: Long): List<ExerciseInstanceWithDetails>

}