package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseKind
import uj.lab.fitnessapp.data.source.Database
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real implementation of [ExerciseRepository].
 * Currently not implemented.
 */
@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val db: Database
) : ExerciseRepository {
    override fun getExerciseKinds(): List<ExerciseKind> = listOf(
        TODO("Not yet implemented")
        // Use db to query exercise instances
    )
}