package uj.lab.fitnessapp.data.source

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import uj.lab.fitnessapp.data.model.*
import uj.lab.fitnessapp.data.repository.ExerciseDao
import uj.lab.fitnessapp.data.repository.ExerciseInstanceDao
import uj.lab.fitnessapp.data.repository.WorkoutSetDao
import java.util.concurrent.Executors

@Database(
    entities = [Exercise::class, ExerciseInstance::class, WorkoutSet::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao() : ExerciseDao
    abstract fun exerciseInstanceDao() : ExerciseInstanceDao
    abstract fun workoutSetDao() : WorkoutSetDao


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val applicationScope = CoroutineScope(SupervisorJob())

        //TODO: zrobić tak, żeby działało z coroutines
        // i bez użycia .allowMainThreadQueries()
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
                .allowMainThreadQueries() //TODO: rozwiązać problem z tym
                .fallbackToDestructiveMigration() //TODO: być może nie jest to najlepsze wyjście
                .addCallback(roomCallback)
                .build()

                INSTANCE = instance
                instance
            }
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                applicationScope.launch(Dispatchers.IO){
                    populateDatabase(INSTANCE!!)
                }


            }
        }

//        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
//        private fun ioThread(f : () -> Unit) {
//            IO_EXECUTOR.execute(f)
//        }

        private suspend fun populateDatabase(db: AppDatabase) {
            //TODO:
            // użycie dao tutaj może być niebezpieczne
            // zmienić na rozwiązanie z ioThread

            val exerciseDao = db.exerciseDao()
            val exerciseInstanceDao = db.exerciseInstanceDao()
            val workoutSetDao = db.workoutSetDao()

            //TODO: tutaj wypełnienie bazy danymi przy pierwszym odpaleniu
            val ex1 = Exercise(0, "Bench Press", false, false, false)
            val ex2 = Exercise(0, "Squat", false, false, false)
            val ex3 = Exercise(0, "Bike", true, false, false)


            exerciseDao.insertExercise(ex1)
            exerciseDao.insertExercise(ex2)
            exerciseDao.insertExercise(ex3)

            val exInst = exerciseDao.getExerciseByName("Bench Press")
            exerciseInstanceDao.insertInstance(ExerciseInstance(0, exInst.id, "01.04.2025"))

            val category = exInst.workoutType// warunkowe wybranie pól do wypełnienia na podstawie tego typu
            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 10, 80.0, null, null))
            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 8, 82.5, null, null))
            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 6, 85.0, null, null))
            workoutSetDao.insertWorkoutSet(WorkoutSet(0, exInst.id, 4, 90.0, null, null))


            Log.i("_____TESTING_____", exerciseInstanceDao.getExerciseInstanceWithDetails(exInst.id).toString())

        }




    }




}