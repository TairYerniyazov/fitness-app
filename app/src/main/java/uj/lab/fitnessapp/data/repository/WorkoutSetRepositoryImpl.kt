package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.WorkoutSet
import uj.lab.fitnessapp.data.source.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkoutSetRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : WorkoutSetRepository {

    private val workoutSetDao = db.workoutSetDao()

    override suspend fun insertWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.insertWorkoutSet(workoutSet)
    override suspend fun updateWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.updateWorkoutSet(workoutSet)
    override suspend fun deleteWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.deleteWorkoutSet(workoutSet)

}