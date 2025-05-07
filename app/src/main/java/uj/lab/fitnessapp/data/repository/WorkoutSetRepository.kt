package uj.lab.fitnessapp.data.repository


import uj.lab.fitnessapp.data.model.WorkoutSet


interface WorkoutSetRepository {

    suspend fun insertWorkoutSet(workoutSet: WorkoutSet)
    suspend fun updateWorkoutSet(workoutSet: WorkoutSet)
    suspend fun deleteWorkoutSet(workoutSet: WorkoutSet)
    suspend fun deleteWorkoutSetsForInstance(instanceId: Int)

}