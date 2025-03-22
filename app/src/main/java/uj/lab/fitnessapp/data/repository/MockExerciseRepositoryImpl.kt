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
        ExerciseKind(1, "Running"),
        ExerciseKind(2, "Swimming"),
        ExerciseKind(3, "Cycling"),
    )
}