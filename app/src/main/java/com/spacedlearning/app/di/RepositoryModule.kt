package com.spacedlearning.app.di

import com.spacedlearning.app.data.repository.AuthRepositoryImpl
import com.spacedlearning.app.data.repository.BookRepositoryImpl
import com.spacedlearning.app.data.repository.ModuleRepositoryImpl
import com.spacedlearning.app.data.repository.ProgressRepositoryImpl
import com.spacedlearning.app.data.repository.RepetitionRepositoryImpl
import com.spacedlearning.app.data.repository.UserRepositoryImpl
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.domain.repository.BookRepository
import com.spacedlearning.app.domain.repository.ModuleRepository
import com.spacedlearning.app.domain.repository.ProgressRepository
import com.spacedlearning.app.domain.repository.RepetitionRepository
import com.spacedlearning.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository

    @Binds
    @Singleton
    abstract fun bindModuleRepository(
        moduleRepositoryImpl: ModuleRepositoryImpl
    ): ModuleRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        progressRepositoryImpl: ProgressRepositoryImpl
    ): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindRepetitionRepository(
        repetitionRepositoryImpl: RepetitionRepositoryImpl
    ): RepetitionRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}