package uj.lab.fitnessapp.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails

@Dao
interface ExerciseInstanceDao {

    @Insert
    suspend fun insertInstance(instance: ExerciseInstance): Long

    @Update
    suspend fun updateInstance(instance: ExerciseInstance)

    @Query("DELETE FROM exerciseInstances WHERE ID = :exerciseID")
    suspend fun deleteInstance(exerciseID: Int)

    @Query("SELECT * FROM exerciseInstances Where exerciseID = :exerciseID")
    suspend fun getExerciseInstanceByExerciseID(exerciseID: Int): List<ExerciseInstance>

    @Transaction
    @Query("SELECT * FROM exerciseInstances WHERE ID = :instanceID")
    suspend fun getExerciseInstanceWithDetails(instanceID: Int): ExerciseInstanceWithDetails

    @Transaction
    @Query("SELECT * FROM exerciseInstances Where exerciseID = :exerciseID")
    suspend fun getExerciseInstanceWithDetailsByExerciseId(exerciseID: Int): List<ExerciseInstanceWithDetails>

    @Transaction
    @Query("SELECT * FROM exerciseInstances WHERE date = :date")
    suspend fun getAllExerciseInstanceWithDetailsForDate(date: Long): List<ExerciseInstanceWithDetails>

    @Transaction
    @Query("SELECT * FROM exerciseInstances WHERE date BETWEEN :startDate AND :endDate AND exerciseID = :exerciseID")
    suspend fun getAllExerciseInstanceWithDetailsInRange(exerciseID: Int, startDate: Long, endDate: Long): List<ExerciseInstanceWithDetails>



}