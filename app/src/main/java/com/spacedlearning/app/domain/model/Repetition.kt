package com.spacedlearning.app.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Repetition(
    val id: UUID,
    val moduleProgressId: UUID,
    val repetitionOrder: RepetitionOrder,
    val status: RepetitionStatus = RepetitionStatus.NOT_STARTED,
    val reviewDate: LocalDate? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

enum class RepetitionOrder {
    FIRST_REPETITION, SECOND_REPETITION, THIRD_REPETITION, FOURTH_REPETITION, FIFTH_REPETITION
}

enum class RepetitionStatus {
    NOT_STARTED, COMPLETED, SKIPPED
}