package com.spacedlearning.app.presentation.screens.modules

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.model.Module
import com.spacedlearning.app.domain.repository.ModuleRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditModuleViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<EditModuleUiState>(EditModuleUiState.Initial)
    val uiState: State<EditModuleUiState> = _uiState

    private val _events = MutableSharedFlow<EditModuleEvent>()
    val events: SharedFlow<EditModuleEvent> = _events.asSharedFlow()

    fun loadModuleDetails(moduleId: String) {
        viewModelScope.launch {
            _uiState.value = EditModuleUiState.Loading

            when (val result = moduleRepository.getModuleById(moduleId)) {
                is Resource.Success -> {
                    _uiState.value = EditModuleUiState.ModuleLoaded(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = EditModuleUiState.Error(result.message)
                    _events.emit(EditModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = EditModuleUiState.Loading
                }
            }
        }
    }

    fun updateModule(
        id: String,
        moduleNo: Int,
        title: String,
        wordCount: Int
    ) {
        viewModelScope.launch {
            _uiState.value = EditModuleUiState.Loading

            when (val result = moduleRepository.updateModule(
                id = id,
                moduleNo = moduleNo,
                title = title,
                wordCount = wordCount
            )) {
                is Resource.Success -> {
                    _events.emit(EditModuleEvent.ModuleUpdated)
                }
                is Resource.Error -> {
                    // Restore previous state
                    val previousState = _uiState.value
                    if (previousState is EditModuleUiState.ModuleLoaded) {
                        _uiState.value = previousState
                    } else {
                        _uiState.value = EditModuleUiState.Error(result.message)
                    }
                    _events.emit(EditModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = EditModuleUiState.Loading
                }
            }
        }
    }
}

sealed class EditModuleUiState {
    object Initial : EditModuleUiState()
    object Loading : EditModuleUiState()
    data class ModuleLoaded(val module: Module) : EditModuleUiState()
    data class Error(val message: String) : EditModuleUiState()
}

sealed class EditModuleEvent {
    data class ShowError(val message: String) : EditModuleEvent()
    object ModuleUpdated : EditModuleEvent()
    object NavigateBack : EditModuleEvent()
}