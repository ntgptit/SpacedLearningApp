package com.spacedlearning.app.data.remote.dto.auth

import com.spacedlearning.app.data.remote.dto.user.UserResponseDto

data class AuthResponseDto(
    val token: String,
    val refreshToken: String?,
    val user: UserResponseDto
)