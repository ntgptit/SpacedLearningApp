package com.spacedlearning.app.presentation.screens.books

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import com.spacedlearning.app.domain.repository.BookRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<CreateBookUiState>(CreateBookUiState.Initial)
    val uiState: State<CreateBookUiState> = _uiState

    private val _events = MutableSharedFlow<CreateBookEvent>()
    val events: SharedFlow<CreateBookEvent> = _events.asSharedFlow()

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CreateBookUiState.Loading

            when (val result = bookRepository.getAllCategories()) {
                is Resource.Success -> {
                    _uiState.value = CreateBookUiState.Success(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = CreateBookUiState.Error(result.message)
                    _events.emit(CreateBookEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = CreateBookUiState.Loading
                }
            }
        }
    }

    fun createBook(
        name: String,
        description: String?,
        status: BookStatus,
        difficultyLevel: DifficultyLevel,
        category: String?
    ) {
        viewModelScope.launch {
            _uiState.value = CreateBookUiState.Loading

            when (val result = bookRepository.createBook(
                name = name,
                description = description,
                status = status,
                difficultyLevel = difficultyLevel,
                category = category
            )) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.let {
                        if (it is CreateBookUiState.Success) {
                            it
                        } else {
                            CreateBookUiState.Success(emptyList())
                        }
                    }
                    _events.emit(CreateBookEvent.BookCreated(result.data.id.toString()))
                }
                is Resource.Error -> {
                    _uiState.value = CreateBookUiState.Error(result.message)
                    _events.emit(CreateBookEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = CreateBookUiState.Loading
                }
            }
        }
    }
}

sealed class CreateBookUiState {
    object Initial : CreateBookUiState()
    object Loading : CreateBookUiState()
    data class Success(val categories: List<String>) : CreateBookUiState()
    data class Error(val message: String) : CreateBookUiState()
}

sealed class CreateBookEvent {
    data class ShowError(val message: String) : CreateBookEvent()
    data class BookCreated(val bookId: String) : CreateBookEvent()
    object NavigateBack : CreateBookEvent()
}