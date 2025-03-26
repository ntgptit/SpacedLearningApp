package com.spacedlearning.app.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val roles: List<String> = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

enum class UserStatus {
    ACTIVE, INACTIVE, SUSPENDED
}