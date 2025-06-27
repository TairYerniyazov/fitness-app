package uj.lab.fitnessapp.data.repository


import uj.lab.fitnessapp.data.model.WorkoutSet


interface WorkoutSetRepository {
    suspend fun insertWorkoutSet(workoutSet: WorkoutSet): Long
    suspend fun updateWorkoutSet(workoutSet: WorkoutSet)
    suspend fun deleteWorkoutSet(workoutSetId: Int)
    suspend fun deleteWorkoutSetsForInstance(instanceId: Int)
    suspend fun getWorkoutSetsForInstance(instanceId: Int): List<WorkoutSet>
}