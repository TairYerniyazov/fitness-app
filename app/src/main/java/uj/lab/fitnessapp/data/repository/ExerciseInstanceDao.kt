package uj.lab.fitnessapp.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails

@Dao
interface ExerciseInstanceDao {

    @Insert
    suspend fun insertInstance(instance: ExerciseInstance)

    @Update
    suspend fun updateInstance(instance: ExerciseInstance)

    @Delete
    suspend fun deleteInstance(instance: ExerciseInstance)

    @Query("SELECT * FROM exerciseInstances Where exerciseID = :exerciseID")
    suspend fun getExerciseInstanceByExerciseID(exerciseID: Int): ExerciseInstance

    @Transaction
    @Query("SELECT * FROM exerciseInstances WHERE ID = :instanceID")
    suspend fun getExerciseInstanceWithDetails(instanceID: Int): ExerciseInstanceWithDetails

}