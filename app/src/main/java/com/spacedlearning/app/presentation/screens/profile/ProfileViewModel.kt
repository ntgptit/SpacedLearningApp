package com.spacedlearning.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.model.User
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.domain.repository.UserRepository
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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = UserState.Loading

            when (val result = userRepository.getCurrentUser()) {
                is Resource.Success -> {
                    _userState.value = UserState.Success(result.data)
                }
                is Resource.Error -> {
                    _userState.value = UserState.Error(result.message)
                    _events.emit(ProfileEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _userState.value = UserState.Loading
                }
            }
        }
    }

    fun updateUserProfile(displayName: String, password: String? = null) {
        viewModelScope.launch {
            val currentUserId = authRepository.getCurrentUserId() ?: return@launch

            when (val result = userRepository.updateUser(
                id = currentUserId,
                displayName = displayName,
                password = password
            )) {
                is Resource.Success -> {
                    _userState.value = UserState.Success(result.data)
                    _events.emit(ProfileEvent.ProfileUpdated)
                }
                is Resource.Error -> {
                    _events.emit(ProfileEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _events.emit(ProfileEvent.LoggedOut)
        }
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}

sealed class ProfileEvent {
    data class ShowError(val message: String) : ProfileEvent()
    object ProfileUpdated : ProfileEvent()
    object LoggedOut : ProfileEvent()
}