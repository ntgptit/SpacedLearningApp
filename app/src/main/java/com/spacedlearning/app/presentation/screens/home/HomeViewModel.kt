package com.spacedlearning.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.domain.repository.ModuleRepository
import com.spacedlearning.app.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _dueTodayCount = MutableStateFlow<Int>(0)
    val dueTodayCount: StateFlow<Int> = _dueTodayCount.asStateFlow()

    init {
        loadUserInfo()
        countDueToday()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            // Logic to load user info would go here
            // For now, we'll just set a placeholder name
            _userName.value = "User"
        }
    }

    private fun countDueToday() {
        viewModelScope.launch {
            // In a real implementation, we'd count the actual due items
            // For now, using a placeholder value
            _dueTodayCount.value = 5
        }
    }
}