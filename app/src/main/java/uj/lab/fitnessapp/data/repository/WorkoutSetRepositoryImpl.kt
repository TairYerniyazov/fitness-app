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

    override suspend fun insertWorkoutSet(workoutSet: WorkoutSet): Long {
        return workoutSetDao.insertWorkoutSet(workoutSet)
    }

    override suspend fun updateWorkoutSet(workoutSet: WorkoutSet) {
        workoutSetDao.updateWorkoutSet(workoutSet)
    }

    override suspend fun deleteWorkoutSet(workoutSetId: Int) {
        workoutSetDao.deleteWorkoutSet(workoutSetId)
    }

    override suspend fun deleteWorkoutSetsForInstance(instanceId: Int) {
        workoutSetDao.deleteWorkoutSetsForInstance(instanceId)
    }

    override suspend fun getWorkoutSetsForInstance(instanceId: Int): List<WorkoutSet> {
        return workoutSetDao.getWorkoutSetsForInstance(instanceId)
    }
}