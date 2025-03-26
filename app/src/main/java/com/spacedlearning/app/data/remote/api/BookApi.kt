package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.book.BookCreateRequestDto
import com.spacedlearning.app.data.remote.dto.book.BookDetailResponseDto
import com.spacedlearning.app.data.remote.dto.book.BookSummaryResponseDto
import com.spacedlearning.app.data.remote.dto.book.BookUpdateRequestDto
import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.PageResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {

    @GET("/api/v1/books")
    suspend fun getAllBooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<BookSummaryResponseDto>

    @GET("/api/v1/books/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): DataResponse<BookDetailResponseDto>

    @GET("/api/v1/books/filter")
    suspend fun filterBooks(
        @Query("status") status: BookStatus? = null,
        @Query("difficultyLevel") difficultyLevel: DifficultyLevel? = null,
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<BookSummaryResponseDto>

    @GET("/api/v1/books/search")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<BookSummaryResponseDto>

    @GET("/api/v1/books/categories")
    suspend fun getAllCategories(): DataResponse<List<String>>

    @POST("/api/v1/books")
    suspend fun createBook(
        @Body bookRequest: BookCreateRequestDto
    ): DataResponse<BookDetailResponseDto>

    @PUT("/api/v1/books/{id}")
    suspend fun updateBook(
        @Path("id") id: String,
        @Body bookRequest: BookUpdateRequestDto
    ): DataResponse<BookDetailResponseDto>

    @DELETE("/api/v1/books/{id}")
    suspend fun deleteBook(
        @Path("id") id: String
    ): SuccessResponse
}