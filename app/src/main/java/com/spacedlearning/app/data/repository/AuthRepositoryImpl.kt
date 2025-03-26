package com.spacedlearning.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spacedlearning.app.data.remote.api.AuthApi
import com.spacedlearning.app.data.remote.dto.auth.AuthRequestDto
import com.spacedlearning.app.data.remote.dto.auth.RefreshTokenRequestDto
import com.spacedlearning.app.data.remote.dto.auth.RegisterRequestDto
import com.spacedlearning.app.domain.model.User
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.util.Constants
import com.spacedlearning.app.util.Resource
import com.spacedlearning.app.util.handleException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val tokenKey = stringPreferencesKey(Constants.TOKEN_PREFS_KEY)
    private val refreshTokenKey = stringPreferencesKey(Constants.REFRESH_TOKEN_PREFS_KEY)
    private val userIdKey = stringPreferencesKey(Constants.USER_ID_PREFS_KEY)

    override suspend fun login(email: String, password: String): Resource<Pair<User, String>> {
        return try {
            val response = authApi.login(AuthRequestDto(email, password))

            // Save token and user ID to DataStore
            saveAuthData(
                token = response.data.token,
                refreshToken = response.data.refreshToken ?: "",
                userId = response.data.user.id.toString()
            )

            // Convert DTO to domain model
            val user = User(
                id = response.data.user.id,
                name = response.data.user.displayName ?: "",
                email = response.data.user.email,
                roles = response.data.user.roles ?: emptyList(),
                createdAt = response.data.user.createdAt
            )

            Resource.success(Pair(user, response.data.token))
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Resource<User> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
            )

            // Convert DTO to domain model
            val user = User(
                id = response.data.id,
                name = response.data.displayName ?: "",
                email = response.data.email,
                roles = response.data.roles ?: emptyList(),
                createdAt = response.data.createdAt
            )

            Resource.success(user)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Resource<String> {
        return try {
            val response = authApi.refreshToken(
                RefreshTokenRequestDto(refreshToken)
            )

            // Save new tokens
            saveAuthData(
                token = response.data.token,
                refreshToken = response.data.refreshToken ?: "",
                userId = response.data.user.id.toString()
            )

            Resource.success(response.data.token)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(userIdKey)
        }
    }

    override suspend fun validateToken(token: String): Resource<Boolean> {
        return try {
            val response = authApi.validateToken(token)
            Resource.success(response.success)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        val token = dataStore.data.map { preferences ->
            preferences[tokenKey]
        }.first()

        return !token.isNullOrEmpty()
    }

    override suspend fun getCurrentUserId(): String? {
        return dataStore.data.map { preferences ->
            preferences[userIdKey]
        }.first()
    }

    private suspend fun saveAuthData(token: String, refreshToken: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
            preferences[refreshTokenKey] = refreshToken
            preferences[userIdKey] = userId
        }
    }
}