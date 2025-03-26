package com.spacedlearning.app.util

object Constants {
    // API
    const val BASE_URL = "https://api.spacedlearning.com"
    const val CONNECT_TIMEOUT = 15L // seconds
    const val READ_TIMEOUT = 30L // seconds
    const val WRITE_TIMEOUT = 30L // seconds

    // Auth
    const val TOKEN_PREFS_KEY = "auth_token"
    const val REFRESH_TOKEN_PREFS_KEY = "refresh_token"
    const val USER_ID_PREFS_KEY = "user_id"

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 3

    // Date formats
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    // Database
    const val DATABASE_NAME = "spaced_learning_db"
    const val DATABASE_VERSION = 1
}