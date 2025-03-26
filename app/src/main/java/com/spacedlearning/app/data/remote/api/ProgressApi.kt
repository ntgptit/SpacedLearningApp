package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.PageResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.data.remote.dto.progress.ModuleProgressCreateRequestDto
import com.spacedlearning.app.data.remote.dto.progress.ModuleProgressDetailResponseDto
import com.spacedlearning.app.data.remote.dto.progress.ModuleProgressSummaryResponseDto
import com.spacedlearning.app.data.remote.dto.progress.ModuleProgressUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProgressApi {

    @GET("/api/v1/progress")
    suspend fun getAllProgress(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleProgressSummaryResponseDto>

    @GET("/api/v1/progress/{id}")
    suspend fun getProgressById(
        @Path("id") id: String
    ): DataResponse<ModuleProgressDetailResponseDto>

    @GET("/api/v1/progress/user/{userId}")
    suspend fun getProgressByUserId(
        @Path("userId") userId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleProgressSummaryResponseDto>

    @GET("/api/v1/progress/module/{moduleId}")
    suspend fun getProgressByModuleId(
        @Path("moduleId") moduleId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleProgressSummaryResponseDto>

    @GET("/api/v1/progress/user/{userId}/book/{bookId}")
    suspend fun getProgressByUserAndBook(
        @Path("userId") userId: String,
        @Path("bookId") bookId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleProgressSummaryResponseDto>

    @GET("/api/v1/progress/user/{userId}/module/{moduleId}")
    suspend fun getProgressByUserAndModule(
        @Path("userId") userId: String,
        @Path("moduleId") moduleId: String
    ): DataResponse<ModuleProgressDetailResponseDto>

    @GET("/api/v1/progress/user/{userId}/due")
    suspend fun getDueProgress(
        @Path("userId") userId: String,
        @Query("studyDate") studyDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleProgressSummaryResponseDto>

    @POST("/api/v1/progress")
    suspend fun createProgress(
        @Body progressRequest: ModuleProgressCreateRequestDto
    ): DataResponse<ModuleProgressDetailResponseDto>

    @PUT("/api/v1/progress/{id}")
    suspend fun updateProgress(
        @Path("id") id: String,
        @Body progressRequest: ModuleProgressUpdateRequestDto
    ): DataResponse<ModuleProgressDetailResponseDto>

    @DELETE("/api/v1/progress/{id}")
    suspend fun deleteProgress(
        @Path("id") id: String
    ): SuccessResponse
}