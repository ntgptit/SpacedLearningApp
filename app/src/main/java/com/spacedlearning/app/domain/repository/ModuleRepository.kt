package com.spacedlearning.app.domain.repository

import androidx.paging.PagingData
import com.spacedlearning.app.domain.model.Module
import com.spacedlearning.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ModuleRepository {
    fun getAllModules(): Flow<PagingData<Module>>

    fun getModulesByBookId(bookId: String): Flow<PagingData<Module>>

    suspend fun getAllModulesByBookId(bookId: String): Resource<List<Module>>

    suspend fun getModuleById(id: String): Resource<Module>

    suspend fun getNextModuleNumber(bookId: String): Resource<Int>

    suspend fun createModule(
        bookId: String,
        moduleNo: Int,
        title: String,
        wordCount: Int?
    ): Resource<Module>

    suspend fun updateModule(
        id: String,
        moduleNo: Int?,
        title: String?,
        wordCount: Int?
    ): Resource<Module>

    suspend fun deleteModule(id: String): Resource<Boolean>
}