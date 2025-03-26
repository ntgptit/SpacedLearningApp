package com.spacedlearning.app.domain.repository

import androidx.paging.PagingData
import com.spacedlearning.app.domain.model.CycleStudied
import com.spacedlearning.app.domain.model.ModuleProgress
import com.spacedlearning.app.util.Resource
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface ProgressRepository {
    fun getAllProgress(): Flow<PagingData<ModuleProgress>>

    fun getProgressByUserId(userId: String): Flow<PagingData<ModuleProgress>>

    fun getProgressByModuleId(moduleId: String): Flow<PagingData<ModuleProgress>>

    fun getProgressByUserAndBook(userId: String, bookId: String): Flow<PagingData<ModuleProgress>>

    fun getDueProgress(userId: String, studyDate: LocalDate? = null): Flow<PagingData<ModuleProgress>>

    suspend fun getProgressById(id: String): Resource<ModuleProgress>

    suspend fun getProgressByUserAndModule(userId: String, moduleId: String): Resource<ModuleProgress>

    suspend fun createProgress(
        moduleId: String,
        userId: String,
        firstLearningDate: LocalDate? = null,
        cyclesStudied: CycleStudied? = null,
        nextStudyDate: LocalDate? = null,
        percentComplete: BigDecimal? = null
    ): Resource<ModuleProgress>

    suspend fun updateProgress(
        id: String,
        firstLearningDate: LocalDate? = null,
        cyclesStudied: CycleStudied? = null,
        nextStudyDate: LocalDate? = null,
        percentComplete: BigDecimal? = null
    ): Resource<ModuleProgress>

    suspend fun deleteProgress(id: String): Resource<Boolean>
}