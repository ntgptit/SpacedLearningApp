package com.spacedlearning.app.data.remote.dto.user

import java.time.LocalDateTime
import java.util.UUID

data class UserResponseDto(
    val id: UUID,
    val email: String,
    val displayName: String?,
    val createdAt: LocalDateTime?,
    val roles: List<String>?
)

data class UserDetailedResponseDto(
    val id: UUID,
    val email: String,
    val displayName: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
)

data class UserUpdateRequestDto(
    val displayName: String?,
    val password: String?
)