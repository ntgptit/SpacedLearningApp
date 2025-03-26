package com.spacedlearning.app.data.remote.dto.common

data class ErrorResponse(
    val message: String,
    val error: String,
    val status: Int
)