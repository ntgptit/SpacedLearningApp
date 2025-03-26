package com.spacedlearning.app.domain.repository

import androidx.paging.PagingData
import com.spacedlearning.app.domain.model.Book
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import com.spacedlearning.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<PagingData<Book>>

    fun getFilteredBooks(
        status: BookStatus? = null,
        difficultyLevel: DifficultyLevel? = null,
        category: String? = null
    ): Flow<PagingData<Book>>

    fun searchBooks(query: String): Flow<PagingData<Book>>

    suspend fun getBookById(id: String): Resource<Book>

    suspend fun getAllCategories(): Resource<List<String>>

    suspend fun createBook(
        name: String,
        description: String?,
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ): Resource<Book>

    suspend fun updateBook(
        id: String,
        name: String?,
        description: String?,
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ): Resource<Book>

    suspend fun deleteBook(id: String): Resource<Boolean>
}