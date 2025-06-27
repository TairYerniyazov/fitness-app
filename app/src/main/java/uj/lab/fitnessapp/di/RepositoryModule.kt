package uj.lab.fitnessapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepository
import uj.lab.fitnessapp.data.repository.ExerciseInstanceRepositoryImpl
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.ExerciseRepositoryImpl
import uj.lab.fitnessapp.data.repository.WorkoutSetRepository
import uj.lab.fitnessapp.data.repository.WorkoutSetRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindExerciseRepository(repository: ExerciseRepositoryImpl): ExerciseRepository

    @Singleton
    @Binds
    abstract fun bindExerciseInstanceRepository(repository: ExerciseInstanceRepositoryImpl): ExerciseInstanceRepository

    @Singleton
    @Binds
    abstract fun bindWorkoutSetRepository(repository: WorkoutSetRepositoryImpl): WorkoutSetRepository
}