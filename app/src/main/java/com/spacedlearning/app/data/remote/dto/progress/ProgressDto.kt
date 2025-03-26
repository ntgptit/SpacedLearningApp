package com.spacedlearning.app.data.remote.dto.progress

import com.spacedlearning.app.data.remote.dto.repetition.RepetitionResponseDto
import com.spacedlearning.app.domain.model.CycleStudied
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ModuleProgressSummaryResponseDto(
    val id: UUID,
    val moduleId: UUID,
    val userId: UUID,
    val firstLearningDate: LocalDate?,
    val cyclesStudied: CycleStudied,
    val nextStudyDate: LocalDate?,
    val percentComplete: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val repetitionCount: Int
)

data class ModuleProgressDetailResponseDto(
    val id: UUID,
    val moduleId: UUID,
    val moduleTitle: String?,
    val userId: UUID,
    val userName: String?,
    val firstLearningDate: LocalDate?,
    val cyclesStudied: CycleStudied,
    val nextStudyDate: LocalDate?,
    val percentComplete: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val repetitions: List<RepetitionResponseDto>?
)

data class ModuleProgressCreateRequestDto(
    val moduleId: UUID,
    val userId: UUID,
    val firstLearningDate: LocalDate?,
    val cyclesStudied: CycleStudied?,
    val nextStudyDate: LocalDate?,
    val percentComplete: BigDecimal?
)

data class ModuleProgressUpdateRequestDto(
    val firstLearningDate: LocalDate?,
    val cyclesStudied: CycleStudied?,
    val nextStudyDate: LocalDate?,
    val percentComplete: BigDecimal?
)