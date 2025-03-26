package com.spacedlearning.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.spacedlearning.app.data.remote.api.BookApi
import com.spacedlearning.app.data.remote.dto.book.BookCreateRequestDto
import com.spacedlearning.app.data.remote.dto.book.BookUpdateRequestDto
import com.spacedlearning.app.domain.model.Book
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import com.spacedlearning.app.domain.repository.BookRepository
import com.spacedlearning.app.util.Constants
import com.spacedlearning.app.util.Resource
import com.spacedlearning.app.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val bookApi: BookApi
) : BookRepository {

    override fun getAllBooks(): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookPagingSource(bookApi) }
        ).flow.map { pagingData ->
            pagingData.map { bookDto ->
                // Map DTO to domain model
                Book(
                    id = bookDto.id,
                    name = bookDto.name,
                    description = null, // Not available in summary response
                    status = bookDto.status,
                    difficultyLevel = bookDto.difficultyLevel,
                    category = bookDto.category,
                    createdAt = bookDto.createdAt,
                    updatedAt = bookDto.updatedAt,
                    moduleCount = bookDto.moduleCount
                )
            }
        }
    }

    override fun getFilteredBooks(
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FilteredBookPagingSource(bookApi, status, difficultyLevel, category)
            }
        ).flow.map { pagingData ->
            pagingData.map { bookDto ->
                // Map DTO to domain model
                Book(
                    id = bookDto.id,
                    name = bookDto.name,
                    description = null, // Not available in summary response
                    status = bookDto.status,
                    difficultyLevel = bookDto.difficultyLevel,
                    category = bookDto.category,
                    createdAt = bookDto.createdAt,
                    updatedAt = bookDto.updatedAt,
                    moduleCount = bookDto.moduleCount
                )
            }
        }
    }

    override fun searchBooks(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchBookPagingSource(bookApi, query) }
        ).flow.map { pagingData ->
            pagingData.map { bookDto ->
                // Map DTO to domain model
                Book(
                    id = bookDto.id,
                    name = bookDto.name,
                    description = null, // Not available in summary response
                    status = bookDto.status,
                    difficultyLevel = bookDto.difficultyLevel,
                    category = bookDto.category,
                    createdAt = bookDto.createdAt,
                    updatedAt = bookDto.updatedAt,
                    moduleCount = bookDto.moduleCount
                )
            }
        }
    }

    override suspend fun getBookById(id: String): Resource<Book> {
        return try {
            val response = bookApi.getBookById(id)
            val bookDto = response.data

            // Map modules if available
            val modules = bookDto.modules?.map { moduleDto ->
                com.spacedlearning.app.domain.model.Module(
                    id = moduleDto.id,
                    bookId = moduleDto.bookId,
                    bookName = moduleDto.bookName,
                    moduleNo = moduleDto.moduleNo,
                    title = moduleDto.title,
                    wordCount = moduleDto.wordCount ?: 0,
                    createdAt = moduleDto.createdAt,
                    updatedAt = moduleDto.updatedAt
                )
            } ?: emptyList()

            // Map DTO to domain model
            val book = Book(
                id = bookDto.id,
                name = bookDto.name,
                description = bookDto.description,
                status = bookDto.status,
                difficultyLevel = bookDto.difficultyLevel,
                category = bookDto.category,
                createdAt = bookDto.createdAt,
                updatedAt = bookDto.updatedAt,
                moduleCount = modules.size,
                modules = modules
            )

            Resource.success(book)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getAllCategories(): Resource<List<String>> {
        return try {
            val response = bookApi.getAllCategories()
            Resource.success(response.data)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun createBook(
        name: String,
        description: String?,
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ): Resource<Book> {
        return try {
            val response = bookApi.createBook(
                BookCreateRequestDto(
                    name = name,
                    description = description,
                    status = status,
                    difficultyLevel = difficultyLevel,
                    category = category
                )
            )

            val bookDto = response.data

            // Map modules if available
            val modules = bookDto.modules?.map { moduleDto ->
                com.spacedlearning.app.domain.model.Module(
                    id = moduleDto.id,
                    bookId = moduleDto.bookId,
                    bookName = moduleDto.bookName,
                    moduleNo = moduleDto.moduleNo,
                    title = moduleDto.title,
                    wordCount = moduleDto.wordCount ?: 0,
                    createdAt = moduleDto.createdAt,
                    updatedAt = moduleDto.updatedAt
                )
            } ?: emptyList()

            // Map DTO to domain model
            val book = Book(
                id = bookDto.id,
                name = bookDto.name,
                description = bookDto.description,
                status = bookDto.status,
                difficultyLevel = bookDto.difficultyLevel,
                category = bookDto.category,
                createdAt = bookDto.createdAt,
                updatedAt = bookDto.updatedAt,
                moduleCount = modules.size,
                modules = modules
            )

            Resource.success(book)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun updateBook(
        id: String,
        name: String?,
        description: String?,
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ): Resource<Book> {
        return try {
            val response = bookApi.updateBook(
                id = id,
                bookRequest = BookUpdateRequestDto(
                    name = name,
                    description = description,
                    status = status,
                    difficultyLevel = difficultyLevel,
                    category = category
                )
            )

            val bookDto = response.data

            // Map modules if available
            val modules = bookDto.modules?.map { moduleDto ->
                com.spacedlearning.app.domain.model.Module(
                    id = moduleDto.id,
                    bookId = moduleDto.bookId,
                    bookName = moduleDto.bookName,
                    moduleNo = moduleDto.moduleNo,
                    title = moduleDto.title,
                    wordCount = moduleDto.wordCount ?: 0,
                    createdAt = moduleDto.createdAt,
                    updatedAt = moduleDto.updatedAt
                )
            } ?: emptyList()

            // Map DTO to domain model
            val book = Book(
                id = bookDto.id,
                name = bookDto.name,
                description = bookDto.description,
                status = bookDto.status,
                difficultyLevel = bookDto.difficultyLevel,
                category = bookDto.category,
                createdAt = bookDto.createdAt,
                updatedAt = bookDto.updatedAt,
                moduleCount = modules.size,
                modules = modules
            )

            Resource.success(book)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun deleteBook(id: String): Resource<Boolean> {
        return try {
            val response = bookApi.deleteBook(id)
            Resource.success(response.success)
        } catch (e: Exception) {
            handleException(e)
        }
    }
}