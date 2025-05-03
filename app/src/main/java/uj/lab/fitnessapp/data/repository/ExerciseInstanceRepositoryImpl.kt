package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseInstance
import uj.lab.fitnessapp.data.model.ExerciseInstanceWithDetails
import uj.lab.fitnessapp.data.source.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExerciseInstanceRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : ExerciseInstanceRepository {

    private val exerciseInstanceDao = db.exerciseInstanceDao()

    override suspend fun insertInstance(instance: ExerciseInstance) = exerciseInstanceDao.insertInstance(instance).toInt()
    override suspend fun updateInstance(instance: ExerciseInstance) = exerciseInstanceDao.updateInstance(instance)
    override suspend fun deleteInstance(exerciseID: Int) = exerciseInstanceDao.deleteInstance(exerciseID)

    override suspend fun getExerciseInstanceByExerciseID(exerciseID: Int): List<ExerciseInstance> {
        return exerciseInstanceDao.getExerciseInstanceByExerciseID(exerciseID)
    }

    override suspend fun getExerciseInstanceWithDetails(instanceID: Int): ExerciseInstanceWithDetails {
        return exerciseInstanceDao.getExerciseInstanceWithDetails(instanceID)
    }

    override suspend fun getAllExerciseInstanceWithDetailsForDate(date: String): List<ExerciseInstanceWithDetails> {
        return exerciseInstanceDao.getAllExerciseInstanceWithDetailsForDate(date)
    }
}