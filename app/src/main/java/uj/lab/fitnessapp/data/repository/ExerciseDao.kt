package uj.lab.fitnessapp.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uj.lab.fitnessapp.data.model.Exercise

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun unsafeDeleteExercise(exercise: Exercise)

    @Query("DELETE FROM exercises WHERE ID = :id AND canModify = 1")
    suspend fun deleteExercise(id: Int)

    @Query("SELECT * FROM exercises ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 1 ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getUserExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE canModify = 0 ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getBasicExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE isFavourite = 1 ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getFavouriteExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 0 ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getStrengthExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE workoutType = 1 ORDER BY exerciseName COLLATE NOCASE")
    suspend fun getCardioExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE exerciseName MATCH :queryName")
    suspend fun searchExercisesByName(queryName: String): List<Exercise>

    @Query("SELECT * FROM exercises WHERE exerciseName = :exerciseName")
    suspend fun getExerciseByName(exerciseName: String): Exercise

    @Query("SELECT EXISTS(SELECT * FROM exercises WHERE exerciseName = :exerciseName)")
    suspend fun checkExerciseExists(exerciseName: String): Boolean

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise

    @Query("SELECT DISTINCT e.* FROM exercises e INNER JOIN exerciseInstances ei ON e.ID = ei.exerciseID")
    suspend fun getExercisesWithInstances(): List<Exercise>

}