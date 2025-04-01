package uj.lab.fitnessapp.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import uj.lab.fitnessapp.data.model.WorkoutSet

@Dao
interface WorkoutSetDao {

    @Insert
    suspend fun insertWorkoutSet(workoutSet: WorkoutSet)

    @Update
    suspend fun updateWorkoutSet(workoutSet: WorkoutSet)

    @Delete
    suspend fun deleteWorkoutSet(workoutSet: WorkoutSet)


}