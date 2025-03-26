package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.PageResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.data.remote.dto.user.UserDetailedResponseDto
import com.spacedlearning.app.data.remote.dto.user.UserResponseDto
import com.spacedlearning.app.data.remote.dto.user.UserUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/api/v1/users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<UserDetailedResponseDto>

    @GET("/api/v1/users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): DataResponse<UserDetailedResponseDto>

    @GET("/api/v1/users/me")
    suspend fun getCurrentUser(): DataResponse<UserResponseDto>

    @PUT("/api/v1/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body userRequest: UserUpdateRequestDto
    ): DataResponse<UserResponseDto>

    @DELETE("/api/v1/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): SuccessResponse

    @POST("/api/v1/users/{id}/restore")
    suspend fun restoreUser(
        @Path("id") id: String
    ): DataResponse<UserResponseDto>
}