package com.spacedlearning.app.data.remote.api

import com.spacedlearning.app.data.remote.dto.auth.AuthRequestDto
import com.spacedlearning.app.data.remote.dto.auth.AuthResponseDto
import com.spacedlearning.app.data.remote.dto.auth.RefreshTokenRequestDto
import com.spacedlearning.app.data.remote.dto.auth.RegisterRequestDto
import com.spacedlearning.app.data.remote.dto.common.DataResponse
import com.spacedlearning.app.data.remote.dto.common.SuccessResponse
import com.spacedlearning.app.data.remote.dto.user.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body authRequest: AuthRequestDto
    ): DataResponse<AuthResponseDto>

    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequestDto
    ): DataResponse<UserResponseDto>

    @POST("/api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequestDto
    ): DataResponse<AuthResponseDto>

    @GET("/api/v1/auth/validate")
    suspend fun validateToken(
        @Query("token") token: String
    ): SuccessResponse
}