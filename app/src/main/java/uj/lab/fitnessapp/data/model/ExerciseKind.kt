package uj.lab.fitnessapp.data.model

// NOTE: This is just an example of a data model
data class ExerciseKind(
    val id: Int,
    val name: String,
    val category: Int, // 0 - cardio, 1 - strength
)