package uj.lab.fitnessapp.data.model

import androidx.room.Embedded
import androidx.room.Relation


class ExerciseInstanceWithDetails {
    @Embedded
    var exerciseInstance: ExerciseInstance? = null

    @Relation(
        parentColumn = "exerciseID",
        entityColumn = "ID",
        entity = Exercise::class
    )
    var exercise: Exercise? = null

    @Relation(
        parentColumn = "ID",
        entityColumn = "instanceID",
        entity = WorkoutSet::class
    )
    var seriesList: List<WorkoutSet>? = null


    override fun toString(): String {
        return "ExerciseInstanceWithDetails(" +
                "exerciseInstance=$exerciseInstance, " +
                "exercise=$exercise, " +
                "seriesList=$seriesList)"
    }

}