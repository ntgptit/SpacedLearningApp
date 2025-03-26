package com.spacedlearning.app.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ModuleProgress(
    val id: UUID,
    val moduleId: UUID,
    val moduleTitle: String? = null,
    val userId: UUID,
    val userName: String? = null,
    val firstLearningDate: LocalDate? = null,
    val cyclesStudied: CycleStudied = CycleStudied.FIRST_TIME,
    val nextStudyDate: LocalDate? = null,
    val percentComplete: BigDecimal = BigDecimal.ZERO,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val repetitions: List<Repetition> = emptyList(),
    val repetitionCount: Int = 0
)

enum class CycleStudied {
    FIRST_TIME, FIRST_REVIEW, SECOND_REVIEW, THIRD_REVIEW, MORE_THAN_THREE_REVIEWS
}