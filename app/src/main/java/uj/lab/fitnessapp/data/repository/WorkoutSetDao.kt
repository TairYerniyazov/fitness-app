package uj.lab.fitnessapp.data.repository

import androidx.room.*
import uj.lab.fitnessapp.data.model.WorkoutSet

@Dao
interface WorkoutSetDao {
    
    @Insert
    suspend fun insertWorkoutSet(workoutSet: WorkoutSet): Long
    
    @Update
    suspend fun updateWorkoutSet(workoutSet: WorkoutSet)
    
    @Query("DELETE FROM workoutSets WHERE ID = :workoutSetId")
    suspend fun deleteWorkoutSet(workoutSetId: Int)
    
    @Query("DELETE FROM workoutSets WHERE instanceID = :instanceId")
    suspend fun deleteWorkoutSetsForInstance(instanceId: Int)
    
    @Query("SELECT * FROM workoutSets WHERE instanceID = :instanceId")
    suspend fun getWorkoutSetsForInstance(instanceId: Int): List<WorkoutSet>
}