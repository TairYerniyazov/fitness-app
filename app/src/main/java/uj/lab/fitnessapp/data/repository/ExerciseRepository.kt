package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.Exercise

interface ExerciseRepository {

    suspend fun insertExercise(exercise: Exercise)
    fun updateExercise(exercise: Exercise)
    fun unsafeDeleteExercise(exercise: Exercise)
    fun deleteExercise(exerciseID: Int)
    suspend fun getAllExercises(): List<Exercise>
    fun getUserExercises(): List<Exercise>
    fun getBasicExercises(): List<Exercise>
    fun getFavouriteExercises(): List<Exercise>
    fun getStrengthExercises(): List<Exercise>
    fun getCardioExercises(): List<Exercise>
    fun searchExerciseByName(queryName: String): List<Exercise>
}