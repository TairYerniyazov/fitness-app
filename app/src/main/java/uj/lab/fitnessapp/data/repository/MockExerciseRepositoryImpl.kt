package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseKind
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of [ExerciseRepository].
 * It's temporary since we don't have database yet.
 */
@Singleton
class MockExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {
    override fun getExerciseKinds(): List<ExerciseKind> = listOf(
        ExerciseKind(1, "Running",0),
        ExerciseKind(2, "Swimming",0),
        ExerciseKind(3, "Cycling",0),
        ExerciseKind(4, "Football",0),
        ExerciseKind(5, "Basketball",0),
        ExerciseKind(6, "Volleyball",0),
        ExerciseKind(7, "Crossfit",1),
        ExerciseKind(8, "Lifting",1),
        ExerciseKind(9, "Bench press",1),
        ExerciseKind(10, "Cricket",0),
        ExerciseKind(11, "Hiking",0),
        ExerciseKind(12, "Baseball",0),
    )
}