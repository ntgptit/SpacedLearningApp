package com.spacedlearning.app.domain.repository

import androidx.paging.PagingData
import com.spacedlearning.app.domain.model.User
import com.spacedlearning.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<PagingData<User>>

    suspend fun getUserById(id: String): Resource<User>

    suspend fun getCurrentUser(): Resource<User>

    suspend fun updateUser(
        id: String,
        displayName: String?,
        password: String?
    ): Resource<User>

    suspend fun deleteUser(id: String): Resource<Boolean>

    suspend fun restoreUser(id: String): Resource<User>
}