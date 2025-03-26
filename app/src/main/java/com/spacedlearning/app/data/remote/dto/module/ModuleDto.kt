package com.spacedlearning.app.data.remote.dto.module

import com.spacedlearning.app.data.remote.dto.progress.ModuleProgressSummaryResponseDto
import java.time.LocalDateTime
import java.util.UUID

data class ModuleSummaryResponseDto(
    val id: UUID,
    val bookId: UUID,
    val moduleNo: Int,
    val title: String,
    val wordCount: Int?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class ModuleDetailResponseDto(
    val id: UUID,
    val bookId: UUID,
    val bookName: String?,
    val moduleNo: Int,
    val title: String,
    val wordCount: Int?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val progress: List<ModuleProgressSummaryResponseDto>?
)

data class ModuleCreateRequestDto(
    val bookId: UUID,
    val moduleNo: Int,
    val title: String,
    val wordCount: Int?
)

data class ModuleUpdateRequestDto(
    val moduleNo: Int?,
    val title: String?,
    val wordCount: Int?
)