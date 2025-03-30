package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.Exercise
import uj.lab.fitnessapp.data.source.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : ExerciseRepository {

    private val exerciseDao = db.exerciseDao()

    override suspend fun insertExercise(exercise: Exercise){
        exerciseDao.insertExercise(exercise)
    }

    override fun updateExercise(exercise: Exercise){
        exerciseDao.updateExercise(exercise)
    }

    override fun unsafeDeleteExercise(exercise: Exercise){
        exerciseDao.unsafeDeleteExercise(exercise)
    }

    override fun deleteExercise(exerciseID: Int){
        exerciseDao.deleteExercise(exerciseID)
    }

    override suspend fun getAllExercises(): List<Exercise> = exerciseDao.getAllExercises()
    override fun getUserExercises(): List<Exercise> = exerciseDao.getUserExercises()
    override fun getBasicExercises(): List<Exercise> = exerciseDao.getBasicExercises()
    override fun getFavouriteExercises(): List<Exercise> = exerciseDao.getFavouriteExercises()
    override fun getStrengthExercises(): List<Exercise> = exerciseDao.getStrengthExercises()
    override fun getCardioExercises(): List<Exercise> = exerciseDao.getCardioExercises()
    override fun searchExerciseByName(queryName: String): List<Exercise> = exerciseDao.searchExerciseByName(queryName)
}


