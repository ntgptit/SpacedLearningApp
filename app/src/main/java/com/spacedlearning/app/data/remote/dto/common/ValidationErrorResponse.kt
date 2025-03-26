package com.spacedlearning.app.data.remote.dto.common

data class ValidationErrorResponse(
    val message: String,
    val errors: List<FieldError>,
    val status: Int
)