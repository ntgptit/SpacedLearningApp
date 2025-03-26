package com.spacedlearning.app.presentation.screens.modules

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.repository.ModuleRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateModuleViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<CreateModuleUiState>(CreateModuleUiState.Initial)
    val uiState: State<CreateModuleUiState> = _uiState

    private val _events = MutableSharedFlow<CreateModuleEvent>()
    val events: SharedFlow<CreateModuleEvent> = _events.asSharedFlow()

    fun getNextModuleNumber(bookId: String) {
        viewModelScope.launch {
            _uiState.value = CreateModuleUiState.Loading

            when (val result = moduleRepository.getNextModuleNumber(bookId)) {
                is Resource.Success -> {
                    _uiState.value = CreateModuleUiState.NextNumberLoaded(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = CreateModuleUiState.Error(result.message)
                    _events.emit(CreateModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = CreateModuleUiState.Loading
                }
            }
        }
    }

    fun createModule(
        bookId: String,
        moduleNo: Int,
        title: String,
        wordCount: Int
    ) {
        viewModelScope.launch {
            _uiState.value = CreateModuleUiState.Loading

            when (val result = moduleRepository.createModule(
                bookId = bookId,
                moduleNo = moduleNo,
                title = title,
                wordCount = wordCount
            )) {
                is Resource.Success -> {
                    _events.emit(CreateModuleEvent.ModuleCreated(result.data.id.toString()))
                }
                is Resource.Error -> {
                    _uiState.value = CreateModuleUiState.Error(result.message)
                    _events.emit(CreateModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = CreateModuleUiState.Loading
                }
            }
        }
    }
}

sealed class CreateModuleUiState {
    object Initial : CreateModuleUiState()
    object Loading : CreateModuleUiState()
    data class NextNumberLoaded(val nextNumber: Int) : CreateModuleUiState()
    data class Error(val message: String) : CreateModuleUiState()
}

sealed class CreateModuleEvent {
    data class ShowError(val message: String) : CreateModuleEvent()
    data class ModuleCreated(val moduleId: String) : CreateModuleEvent()
    object NavigateBack : CreateModuleEvent()
}