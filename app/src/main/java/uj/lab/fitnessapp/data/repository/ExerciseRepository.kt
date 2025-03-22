package uj.lab.fitnessapp.data.repository

import uj.lab.fitnessapp.data.model.ExerciseKind

interface ExerciseRepository {
    fun getExerciseKinds(): List<ExerciseKind>
}