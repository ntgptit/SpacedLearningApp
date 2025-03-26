package com.spacedlearning.app.data.remote.dto.book

import com.spacedlearning.app.data.remote.dto.module.ModuleDetailResponseDto
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import java.time.LocalDateTime
import java.util.UUID


data class BookSummaryResponseDto(
    val id: UUID,
    val name: String,
    val status: BookStatus,
    val difficultyLevel: DifficultyLevel?,
    val category: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val moduleCount: Int
)

data class BookDetailResponseDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val status: BookStatus,
    val difficultyLevel: DifficultyLevel?,
    val category: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val modules: List<ModuleDetailResponseDto>?
)

data class BookCreateRequestDto(
    val name: String,
    val description: String?,
    val status: BookStatus?,
    val difficultyLevel: DifficultyLevel?,
    val category: String?
)

data class BookUpdateRequestDto(
    val name: String?,
    val description: String?,
    val status: BookStatus?,
    val difficultyLevel: DifficultyLevel?,
    val category: String?
)