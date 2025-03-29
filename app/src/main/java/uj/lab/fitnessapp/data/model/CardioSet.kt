package uj.lab.fitnessapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cardioSets",
    foreignKeys = [ForeignKey(
        entity = ExerciseInstance::class,
        parentColumns = ["instanceID"],
        childColumns = ["instanceID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CardioSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cardioSetID") val cardioSetID: Int,
    @ColumnInfo(name = "instanceID") val instanceID: Int,
    @ColumnInfo(name = "time") val time: Int, //TODO: zmienić na S/M/H
    @ColumnInfo(name = "distance") val distance: Int //metry
)
