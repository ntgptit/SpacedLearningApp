package com.spacedlearning.app.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authEvents = MutableSharedFlow<AuthEvent>()
    val authEvents: SharedFlow<AuthEvent> = _authEvents.asSharedFlow()

    init {
        checkLoginStatus()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading

            // Validate inputs
            if (email.isBlank() || password.isBlank()) {
                _loginState.value = AuthState.Error("Email and password cannot be empty")
                return@launch
            }

            when (val result = authRepository.login(email, password)) {
                is Resource.Success -> {
                    _isLoggedIn.value = true
                    _loginState.value = AuthState.Success
                    _authEvents.emit(AuthEvent.LoginSuccess)
                }
                is Resource.Error -> {
                    _loginState.value = AuthState.Error(result.message)
                }
                is Resource.Loading -> {
                    _loginState.value = AuthState.Loading
                }
            }
        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading

            // Validate inputs
            if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                _registerState.value = AuthState.Error("All fields are required")
                return@launch
            }

            when (val result = authRepository.register(email, password, firstName, lastName)) {
                is Resource.Success -> {
                    _registerState.value = AuthState.Success
                    _authEvents.emit(AuthEvent.RegisterSuccess)
                }
                is Resource.Error -> {
                    _registerState.value = AuthState.Error(result.message)
                }
                is Resource.Loading -> {
                    _registerState.value = AuthState.Loading
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _authEvents.emit(AuthEvent.LogoutSuccess)
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = authRepository.isLoggedIn()
        }
    }

    fun resetLoginState() {
        _loginState.value = AuthState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class AuthEvent {
    object LoginSuccess : AuthEvent()
    object RegisterSuccess : AuthEvent()
    object LogoutSuccess : AuthEvent()
}