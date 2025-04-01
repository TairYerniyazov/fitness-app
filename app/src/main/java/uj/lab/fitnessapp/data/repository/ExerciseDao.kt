package uj.lab.fitnessapp.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uj.lab.fitnessapp.data.model.Exercise

@Dao
interface ExerciseDao {
    //TODO: ogarnąć suspend
    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun unsafeDeleteExercise(exercise: Exercise)

    //TODO: zastanowic się, czy tutaj tego nie robić inaczej
    @Query("DELETE FROM exercises WHERE ID = :id AND canModify = 1")
    suspend fun deleteExercise(id: Int)

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 1")
    suspend fun getUserExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 0")
    suspend fun getBasicExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE isFavourite = 1")
    suspend fun getFavouriteExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 0")
    suspend fun getStrengthExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 1")
    suspend fun getCardioExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE exerciseName MATCH :queryName")
    suspend fun searchExercisesByName(queryName: String): List<Exercise>

    @Query("SELECT * FROM exercises WHERE exerciseName = :exerciseName")
    suspend fun getExerciseByName(exerciseName: String): Exercise


}