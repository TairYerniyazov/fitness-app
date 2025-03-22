package uj.lab.fitnessapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uj.lab.fitnessapp.data.repository.ExerciseRepository
import uj.lab.fitnessapp.data.repository.MockExerciseRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Injects the mock repository implementation
    // Once the real implementation is ready, this should be changed to the real implementation
    @Singleton
    @Binds
    abstract fun bindExerciseRepository(repository: MockExerciseRepositoryImpl): ExerciseRepository
}