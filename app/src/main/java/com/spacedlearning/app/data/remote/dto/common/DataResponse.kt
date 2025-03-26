package com.spacedlearning.app.data.remote.dto.common

data class DataResponse<T>(
    val data: T,
    val success: Boolean
)