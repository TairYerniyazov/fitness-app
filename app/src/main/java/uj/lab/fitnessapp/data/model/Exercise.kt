package uj.lab.fitnessapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "exerciseName")val exerciseName: String,
    @ColumnInfo(name = "workoutType") val workoutType: Boolean,
    @ColumnInfo(name = "canModify") val canModify: Boolean,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean
)
