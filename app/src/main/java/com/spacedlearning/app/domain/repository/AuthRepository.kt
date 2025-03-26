package com.spacedlearning.app.domain.repository

import com.spacedlearning.app.domain.model.User
import com.spacedlearning.app.util.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Pair<User, String>>
    suspend fun register(email: String, password: String, firstName: String, lastName: String): Resource<User>
    suspend fun refreshToken(refreshToken: String): Resource<String>
    suspend fun logout()
    suspend fun validateToken(token: String): Resource<Boolean>
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUserId(): String?
}