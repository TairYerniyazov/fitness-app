package uj.lab.fitnessapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "strengthSets",
    foreignKeys = [ForeignKey(
        entity = ExerciseInstance::class,
        parentColumns = ["ID"],
        childColumns = ["instanceID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class StrengthSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "instanceID") val instanceID: Int,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "load") val load: Int //kg
)