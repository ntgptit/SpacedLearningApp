package com.spacedlearning.app.data.remote.dto.repetition

import com.spacedlearning.app.domain.model.RepetitionOrder
import com.spacedlearning.app.domain.model.RepetitionStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RepetitionResponseDto(
    val id: UUID,
    val moduleProgressId: UUID,
    val repetitionOrder: RepetitionOrder,
    val status: RepetitionStatus,
    val reviewDate: LocalDate?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class RepetitionCreateRequestDto(
    val moduleProgressId: UUID,
    val repetitionOrder: RepetitionOrder,
    val status: RepetitionStatus?,
    val reviewDate: LocalDate?
)

data class RepetitionUpdateRequestDto(
    val status: RepetitionStatus?,
    val reviewDate: LocalDate?
)