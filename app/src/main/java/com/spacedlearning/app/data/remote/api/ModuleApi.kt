package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.PageResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.data.remote.dto.module.ModuleCreateRequestDto
import com.spacedlearning.app.data.remote.dto.module.ModuleDetailResponseDto
import com.spacedlearning.app.data.remote.dto.module.ModuleSummaryResponseDto
import com.spacedlearning.app.data.remote.dto.module.ModuleUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ModuleApi {

    @GET("/api/v1/modules")
    suspend fun getAllModules(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleSummaryResponseDto>

    @GET("/api/v1/modules/{id}")
    suspend fun getModuleById(
        @Path("id") id: String
    ): DataResponse<ModuleDetailResponseDto>

    @GET("/api/v1/modules/book/{bookId}")
    suspend fun getModulesByBookId(
        @Path("bookId") bookId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<ModuleSummaryResponseDto>

    @GET("/api/v1/modules/book/{bookId}/all")
    suspend fun getAllModulesByBookId(
        @Path("bookId") bookId: String
    ): DataResponse<List<ModuleSummaryResponseDto>>

    @GET("/api/v1/modules/book/{bookId}/next-number")
    suspend fun getNextModuleNumber(
        @Path("bookId") bookId: String
    ): DataResponse<Int>

    @POST("/api/v1/modules")
    suspend fun createModule(
        @Body moduleRequest: ModuleCreateRequestDto
    ): DataResponse<ModuleDetailResponseDto>

    @PUT("/api/v1/modules/{id}")
    suspend fun updateModule(
        @Path("id") id: String,
        @Body moduleRequest: ModuleUpdateRequestDto
    ): DataResponse<ModuleDetailResponseDto>

    @DELETE("/api/v1/modules/{id}")
    suspend fun deleteModule(
        @Path("id") id: String
    ): SuccessResponse
}