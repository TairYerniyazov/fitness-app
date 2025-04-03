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

//            val exerciseInstanceDao = db.exerciseInstanceDao()
//            val workoutSetDao = db.workoutSetDao()
//
//            //tutaj wypełnienie bazy danymi przy pierwszym odpaleniu
//            val ex1 = Exercise(0, "Bench Press", false, false, false)
//            val ex2 = Exercise(0, "Squat", false, false, false)
//            val ex3 = Exercise(0, "Bike", true, false, false)
//
//            exerciseDao.insertExercise(ex1)
//            exerciseDao.insertExercise(ex2)
//            exerciseDao.insertExercise(ex3)
//
//            val exInst = exerciseDao.getExerciseByName("Bench Press")
//            exerciseInstanceDao.insertInstance(ExerciseInstance(0, exInst.id, "01.04.2025"))
//
//            val category = exInst.workoutType// warunkowe wybranie pól do wypełnienia na podstawie tego typu
//            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 10, 80.0, null, null))
//            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 8, 82.5, null, null))
//            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 6, 85.0, null, null))
//            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 4, 90.0, null, null))
//
//            Log.i("_____TESTING_____", exerciseInstanceDao.getExerciseInstanceWithDetails(exInst.id).toString())

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
                    .fallbackToDestructiveMigration() //TODO: to może być nienajlepsze wyjście, chociaż dla nas może być ok
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}