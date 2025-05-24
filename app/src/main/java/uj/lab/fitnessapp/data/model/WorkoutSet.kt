package uj.lab.fitnessapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "workoutSets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseInstance::class,
            parentColumns = ["ID"],
            childColumns = ["instanceID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "instanceID") val instanceID: Int,
    @ColumnInfo(name = "reps") val reps: Int? = null,
    @ColumnInfo(name = "load") val load: Double? = null, //kg
    @ColumnInfo(name = "time") val time: Int? = null, //HH:MM:SS
    @ColumnInfo(name = "distance") val distance: Double? = null //m

)