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
    fun updateExercise(exercise: Exercise)

    @Delete
    fun unsafeDeleteExercise(exercise: Exercise)

    //TODO: zastanowic się, czy tutaj tego nie robić inaczej
    @Query("DELETE FROM exercises WHERE ID = :id AND canModify = 1")
    fun deleteExercise(id: Int)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 1")
    fun getUserExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 0")
    fun getBasicExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE isFavourite = 1")
    fun getFavouriteExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 0")
    fun getStrengthExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 1")
    fun getCardioExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE exerciseName MATCH :queryName")
    fun searchExerciseByName(queryName: String): List<Exercise>
}