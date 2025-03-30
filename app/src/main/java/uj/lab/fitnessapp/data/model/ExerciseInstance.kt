package uj.lab.fitnessapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "exerciseInstances",
    foreignKeys = [ForeignKey(
        entity = Exercise::class,
        parentColumns = ["ID"],
        childColumns = ["exerciseID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExerciseInstance(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "exerciseID") val exerciseID: Int,
    @ColumnInfo(name = "date") val date: String, // TODO: zamienić ten typ na D/M/Y
)