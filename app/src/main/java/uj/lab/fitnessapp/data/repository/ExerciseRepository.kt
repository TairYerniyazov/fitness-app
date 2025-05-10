package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.Exercise

interface ExerciseRepository {

    suspend fun insertExercise(exercise: Exercise)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun unsafeDeleteExercise(exercise: Exercise)
    suspend fun deleteExercise(exerciseID: Int)
    suspend fun getAllExercises(): List<Exercise>
    suspend fun getUserExercises(): List<Exercise>
    suspend fun getBasicExercises(): List<Exercise>
    suspend fun getFavouriteExercises(): List<Exercise>
    suspend fun getStrengthExercises(): List<Exercise>
    suspend fun getCardioExercises(): List<Exercise>
    suspend fun searchExercisesByName(queryName: String): List<Exercise>
    suspend fun getExerciseByName(exerciseName: String): Exercise
    suspend fun getExercisesWithInstances(): List<Exercise>

}