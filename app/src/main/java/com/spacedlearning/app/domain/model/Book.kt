package com.spacedlearning.app.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Book(
    val id: UUID,
    val name: String,
    val description: String?,
    val status: BookStatus,
    val difficultyLevel: DifficultyLevel?,
    val category: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val moduleCount: Int = 0,
    val modules: List<Module> = emptyList()
)

enum class BookStatus {
    PUBLISHED, DRAFT, ARCHIVED
}

enum class DifficultyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}