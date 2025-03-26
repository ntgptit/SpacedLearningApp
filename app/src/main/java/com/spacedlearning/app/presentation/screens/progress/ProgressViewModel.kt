package com.spacedlearning.app.presentation.screens.progress

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.spacedlearning.app.domain.model.ModuleProgress
import com.spacedlearning.app.domain.model.RepetitionStatus
import com.spacedlearning.app.domain.repository.AuthRepository
import com.spacedlearning.app.domain.repository.ProgressRepository
import com.spacedlearning.app.domain.repository.RepetitionRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val repetitionRepository: RepetitionRepository,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _progressDetailState = MutableStateFlow<ProgressDetailState>(ProgressDetailState.Loading)
    val progressDetailState: StateFlow<ProgressDetailState> = _progressDetailState.asStateFlow()

    private val _events = MutableSharedFlow<ProgressEvent>()
    val events: SharedFlow<ProgressEvent> = _events.asSharedFlow()

    private var userId: String? = null

    init {
        loadUserId()
    }

    private fun loadUserId() {
        viewModelScope.launch {
            userId = authRepository.getCurrentUserId()
        }
    }

    val userProgress: Flow<PagingData<ModuleProgress>> =
        progressRepository.getProgressByUserId(userId ?: "")
            .cachedIn(viewModelScope)

    @RequiresApi(Build.VERSION_CODES.O)
    val dueProgress: Flow<PagingData<ModuleProgress>> =
        progressRepository.getDueProgress(userId ?: "", LocalDate.now())
            .cachedIn(viewModelScope)

    fun loadProgressDetail(progressId: String) {
        viewModelScope.launch {
            _progressDetailState.value = ProgressDetailState.Loading

            when (val result = progressRepository.getProgressById(progressId)) {
                is Resource.Success -> {
                    _progressDetailState.value = ProgressDetailState.Success(result.data)
                }
                is Resource.Error -> {
                    _progressDetailState.value = ProgressDetailState.Error(result.message)
                    _events.emit(ProgressEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _progressDetailState.value = ProgressDetailState.Loading
                }
            }
        }
    }

    fun updateProgress(
        progressId: String,
        percentComplete: Float
    ) {
        viewModelScope.launch {
            val updateResult = progressRepository.updateProgress(
                id = progressId,
                percentComplete = java.math.BigDecimal.valueOf(percentComplete.toDouble())
            )

            when (updateResult) {
                is Resource.Success -> {
                    _events.emit(ProgressEvent.ProgressUpdated)
                    loadProgressDetail(progressId)
                }
                is Resource.Error -> {
                    _events.emit(ProgressEvent.ShowError(updateResult.message))
                }
                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    fun completeRepetition(repetitionId: String) {
        viewModelScope.launch {
            val updateResult = repetitionRepository.updateRepetition(
                id = repetitionId,
                status = RepetitionStatus.COMPLETED,
                reviewDate = null
            )

            when (updateResult) {
                is Resource.Success -> {
                    _events.emit(ProgressEvent.RepetitionCompleted)
                }
                is Resource.Error -> {
                    _events.emit(ProgressEvent.ShowError(updateResult.message))
                }
                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun skipRepetition(repetitionId: String) {
        viewModelScope.launch {
            val updateResult = repetitionRepository.updateRepetition(
                id = repetitionId,
                status = RepetitionStatus.SKIPPED,
                reviewDate = LocalDate.now().plusDays(1)
            )

            when (updateResult) {
                is Resource.Success -> {
                    _events.emit(ProgressEvent.RepetitionSkipped)
                }
                is Resource.Error -> {
                    _events.emit(ProgressEvent.ShowError(updateResult.message))
                }
                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }
}

sealed class ProgressDetailState {
    object Loading : ProgressDetailState()
    data class Success(val progress: ModuleProgress) : ProgressDetailState()
    data class Error(val message: String) : ProgressDetailState()
}

sealed class ProgressEvent {
    data class ShowError(val message: String) : ProgressEvent()
    object ProgressUpdated : ProgressEvent()
    object RepetitionCompleted : ProgressEvent()
    object RepetitionSkipped : ProgressEvent()
    object NavigateBack : ProgressEvent()
}