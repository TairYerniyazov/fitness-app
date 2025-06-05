package uj.lab.fitnessapp.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import uj.lab.fitnessapp.R

import uj.lab.fitnessapp.data.model.*
import uj.lab.fitnessapp.data.repository.ExerciseDao
import uj.lab.fitnessapp.data.repository.ExerciseInstanceDao
import uj.lab.fitnessapp.data.repository.WorkoutSetDao
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import androidx.core.content.edit


@Database(
    entities = [Exercise::class, ExerciseInstance::class, WorkoutSet::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseInstanceDao(): ExerciseInstanceDao
    abstract fun workoutSetDao(): WorkoutSetDao

    suspend fun populateDatabase(context: Context) {
        if (IS_POPULATED.get()) {
            return
        }
        val prefs = context.getSharedPreferences("uj.lab.fitnessapp", Context.MODE_PRIVATE)
        val isInitialized = prefs.getBoolean("isInitialized", false)
        if (isInitialized) {
            return
        }

        prefs.edit() { putBoolean("isInitialized", true) }

        val exerciseDao = exerciseDao()
        val exerciseList: JSONArray =
            context.resources.openRawResource(R.raw.default_exercises).bufferedReader().use {
                JSONArray(it.readText())
            }

        exerciseList.takeIf { it.length() > 0 }?.let { list ->
            for (index in 0 until list.length()) {
                val exerciseObj = list.getJSONObject(index)
                exerciseDao.insertExercise(
                    Exercise(
                        exerciseObj.getInt("exerciseId"),
                        exerciseObj.getString("exerciseName"),
                        WorkoutType.valueOf(exerciseObj.getString("workoutType")),
                        exerciseObj.getBoolean("canModify"),
                        exerciseObj.getBoolean("isFavourite")
                    )
                )

            }
        }

        IS_POPULATED.set(true)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val IS_POPULATED = AtomicBoolean(false)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDatabase"
                )
                    .setQueryCallback(QueryCallback { sqlQuery, bindArgs ->
                        println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                    }, Executors.newSingleThreadExecutor())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}