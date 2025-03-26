package com.spacedlearning.app.data.remote.dto.auth

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)