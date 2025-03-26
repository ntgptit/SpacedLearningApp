package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.PageResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.data.remote.dto.repetition.RepetitionCreateRequestDto
import com.spacedlearning.app.data.remote.dto.repetition.RepetitionResponseDto
import com.spacedlearning.app.data.remote.dto.repetition.RepetitionUpdateRequestDto
import com.spacedlearning.app.domain.model.RepetitionOrder
import com.spacedlearning.app.domain.model.RepetitionStatus
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RepetitionApi {

    @GET("/api/v1/repetitions")
    suspend fun getAllRepetitions(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<RepetitionResponseDto>

    @GET("/api/v1/repetitions/{id}")
    suspend fun getRepetitionById(
        @Path("id") id: String
    ): DataResponse<RepetitionResponseDto>

    @GET("/api/v1/repetitions/progress/{progressId}")
    suspend fun getRepetitionsByProgressId(
        @Path("progressId") progressId: String
    ): DataResponse<List<RepetitionResponseDto>>

    @GET("/api/v1/repetitions/progress/{progressId}/order/{order}")
    suspend fun getRepetitionByProgressIdAndOrder(
        @Path("progressId") progressId: String,
        @Path("order") order: RepetitionOrder
    ): DataResponse<RepetitionResponseDto>

    @GET("/api/v1/repetitions/user/{userId}/due")
    suspend fun getDueRepetitions(
        @Path("userId") userId: String,
        @Query("reviewDate") reviewDate: String? = null,
        @Query("status") status: RepetitionStatus? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<RepetitionResponseDto>

    @POST("/api/v1/repetitions")
    suspend fun createRepetition(
        @Body repetitionRequest: RepetitionCreateRequestDto
    ): DataResponse<RepetitionResponseDto>

    @POST("/api/v1/repetitions/progress/{progressId}/schedule")
    suspend fun createDefaultSchedule(
        @Path("progressId") progressId: String
    ): DataResponse<List<RepetitionResponseDto>>

    @PUT("/api/v1/repetitions/{id}")
    suspend fun updateRepetition(
        @Path("id") id: String,
        @Body repetitionRequest: RepetitionUpdateRequestDto
    ): DataResponse<RepetitionResponseDto>

    @DELETE("/api/v1/repetitions/{id}")
    suspend fun deleteRepetition(
        @Path("id") id: String
    ): SuccessResponse
}