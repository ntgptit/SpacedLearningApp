package com.spacedlearning.app.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Module(
    val id: UUID,
    val bookId: UUID,
    val bookName: String? = null,
    val moduleNo: Int,
    val title: String,
    val wordCount: Int = 0,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val progress: List<ModuleProgress> = emptyList()
)
