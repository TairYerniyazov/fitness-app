package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails


interface ExerciseInstanceRepository {

    suspend fun insertInstance(instance: ExerciseInstance)
    suspend fun updateInstance(instance: ExerciseInstance)
    suspend fun deleteInstance(instance: ExerciseInstance)
    suspend fun getExerciseInstanceByExerciseID(exerciseID: Int): ExerciseInstance
    suspend fun getExerciseInstanceWithDetails(instanceID: Int): ExerciseInstanceWithDetails

}