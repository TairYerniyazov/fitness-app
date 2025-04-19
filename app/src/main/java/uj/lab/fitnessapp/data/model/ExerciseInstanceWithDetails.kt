package uj.lab.fitnessapp.data.model

import androidx.room.Embedded
import androidx.room.Relation


data class ExerciseInstanceWithDetails(
    @Embedded
    val exerciseInstance: ExerciseInstance? = null,

    @Relation(
        parentColumn = "exerciseID",
        entityColumn = "ID",
        entity = Exercise::class
    )
    val exercise: Exercise? = null,

    @Relation(
        parentColumn = "ID",
        entityColumn = "instanceID",
        entity = WorkoutSet::class
    )
    val seriesList: List<WorkoutSet>? = null
)