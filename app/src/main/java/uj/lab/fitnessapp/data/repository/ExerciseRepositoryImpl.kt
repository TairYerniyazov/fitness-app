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

    override suspend fun updateExercise(exercise: Exercise){
        exerciseDao.updateExercise(exercise)
    }

    override suspend fun unsafeDeleteExercise(exercise: Exercise){
        exerciseDao.unsafeDeleteExercise(exercise)
    }

    override suspend fun deleteExercise(exerciseID: Int){
        exerciseDao.deleteExercise(exerciseID)
    }

    override suspend fun getAllExercises(): List<Exercise> = exerciseDao.getAllExercises()
    override suspend fun getUserExercises(): List<Exercise> = exerciseDao.getUserExercises()
    override suspend fun getBasicExercises(): List<Exercise> = exerciseDao.getBasicExercises()
    override suspend fun getFavouriteExercises(): List<Exercise> = exerciseDao.getFavouriteExercises()
    override suspend fun getStrengthExercises(): List<Exercise> = exerciseDao.getStrengthExercises()
    override suspend fun getCardioExercises(): List<Exercise> = exerciseDao.getCardioExercises()
    override suspend fun searchExercisesByName(queryName: String): List<Exercise> = exerciseDao.searchExercisesByName(queryName)
    override suspend fun getExerciseByName(exerciseName: String): Exercise = exerciseDao.getExerciseByName(exerciseName)

}


