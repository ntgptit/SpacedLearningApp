package com.spacedlearning.app.presentation.screens.repetition

import com.spacedlearning.app.domain.model.Repetition

// UI States
sealed class RepetitionUiState {
    object Loading : RepetitionUiState()
    data class Success(val repetitions: List<Repetition>) : RepetitionUiState()
    data class Error(val message: String) : RepetitionUiState()
}

// Events
sealed class RepetitionEvent {
    data class ShowError(val message: String) : RepetitionEvent()
    object RepetitionUpdated : RepetitionEvent()
    object NavigateBack : RepetitionEvent()
}