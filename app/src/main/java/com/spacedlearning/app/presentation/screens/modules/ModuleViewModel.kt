package com.spacedlearning.app.presentation.screens.modules

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.spacedlearning.app.domain.model.Module
import com.spacedlearning.app.domain.repository.ModuleRepository
import com.spacedlearning.app.domain.repository.ProgressRepository
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
class ModuleViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val progressRepository: ProgressRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _moduleDetailState = MutableStateFlow<ModuleDetailState>(ModuleDetailState.Loading)
    val moduleDetailState: StateFlow<ModuleDetailState> = _moduleDetailState.asStateFlow()

    private val _moduleListState = MutableStateFlow<ModuleListState>(ModuleListState.Loading)
    val moduleListState: StateFlow<ModuleListState> = _moduleListState.asStateFlow()

    private val _events = MutableSharedFlow<ModuleEvent>()
    val events: SharedFlow<ModuleEvent> = _events.asSharedFlow()

    fun loadModuleDetail(moduleId: String) {
        viewModelScope.launch {
            _moduleDetailState.value = ModuleDetailState.Loading

            when (val result = moduleRepository.getModuleById(moduleId)) {
                is Resource.Success -> {
                    _moduleDetailState.value = ModuleDetailState.Success(result.data)
                }
                is Resource.Error -> {
                    _moduleDetailState.value = ModuleDetailState.Error(result.message)
                    _events.emit(ModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _moduleDetailState.value = ModuleDetailState.Loading
                }
            }
        }
    }

    fun loadModulesByBookId(bookId: String) {
        viewModelScope.launch {
            _moduleListState.value = ModuleListState.Loading

            when (val result = moduleRepository.getAllModulesByBookId(bookId)) {
                is Resource.Success -> {
                    _moduleListState.value = ModuleListState.Success(result.data)
                }
                is Resource.Error -> {
                    _moduleListState.value = ModuleListState.Error(result.message)
                    _events.emit(ModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _moduleListState.value = ModuleListState.Loading
                }
            }
        }
    }

    fun createModuleProgress(userId: String, moduleId: String) {
        viewModelScope.launch {
            when (val result = progressRepository.createProgress(
                moduleId = moduleId,
                userId = userId
            )) {
                is Resource.Success -> {
                    _events.emit(ModuleEvent.ProgressCreated)
                }
                is Resource.Error -> {
                    _events.emit(ModuleEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    // Handle loading state
                }
            }
        }
    }
}

sealed class ModuleDetailState {
    object Loading : ModuleDetailState()
    data class Success(val module: Module) : ModuleDetailState()
    data class Error(val message: String) : ModuleDetailState()
}

sealed class ModuleListState {
    object Loading : ModuleListState()
    data class Success(val modules: List<Module>) : ModuleListState()
    data class Error(val message: String) : ModuleListState()
}

sealed class ModuleEvent {
    data class ShowError(val message: String) : ModuleEvent()
    object ProgressCreated : ModuleEvent()
    object NavigateBack : ModuleEvent()
}