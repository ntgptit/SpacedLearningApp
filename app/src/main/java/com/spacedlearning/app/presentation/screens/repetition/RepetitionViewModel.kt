package com.spacedlearning.app.presentation.screens.repetition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.model.Repetition
import com.spacedlearning.app.domain.model.RepetitionStatus
import com.spacedlearning.app.domain.repository.RepetitionRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RepetitionViewModel @Inject constructor(
    private val repetitionRepository: RepetitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RepetitionUiState>(RepetitionUiState.Loading)
    val uiState: StateFlow<RepetitionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RepetitionEvent>()
    val events: SharedFlow<RepetitionEvent> = _events.asSharedFlow()

    fun loadRepetitions(progressId: String) {
        viewModelScope.launch {
            _uiState.value = RepetitionUiState.Loading

            when (val result = repetitionRepository.getRepetitionsByProgressId(progressId)) {
                is Resource.Success -> {
                    _uiState.value = RepetitionUiState.Success(result.data)
                }

                is Resource.Error -> {
                    _uiState.value = RepetitionUiState.Error(result.message)
                    _events.emit(RepetitionEvent.ShowError(result.message))
                }

                is Resource.Loading -> {
                    _uiState.value = RepetitionUiState.Loading
                }
            }
        }
    }

    fun createDefaultSchedule(progressId: String) {
        viewModelScope.launch {
            when (val result = repetitionRepository.createDefaultSchedule(progressId)) {
                is Resource.Success -> {
                    _uiState.value = RepetitionUiState.Success(result.data)
                    _events.emit(RepetitionEvent.RepetitionUpdated)
                }

                is Resource.Error -> {
                    _events.emit(RepetitionEvent.ShowError(result.message))
                }

                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    fun updateRepetitionStatus(
        repetitionId: String,
        status: RepetitionStatus
    ) {
        viewModelScope.launch {
            when (val result = repetitionRepository.updateRepetition(
                id = repetitionId,
                status = status,
                reviewDate = null // Keep existing date
            )) {
                is Resource.Success -> {
                    _events.emit(RepetitionEvent.RepetitionUpdated)
                }

                is Resource.Error -> {
                    _events.emit(RepetitionEvent.ShowError(result.message))
                }

                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    fun updateRepetitionDate(
        repetitionId: String,
        date: LocalDate
    ) {
        viewModelScope.launch {
            when (val result = repetitionRepository.updateRepetition(
                id = repetitionId,
                status = null, // Keep existing status
                reviewDate = date
            )) {
                is Resource.Success -> {
                    _events.emit(RepetitionEvent.RepetitionUpdated)
                }

                is Resource.Error -> {
                    _events.emit(RepetitionEvent.ShowError(result.message))
                }

                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }
}