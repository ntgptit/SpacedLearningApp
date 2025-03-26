package com.spacedlearning.app.presentation.screens.books

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacedlearning.app.domain.model.Book
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
class EditBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<EditBookUiState>(EditBookUiState.Initial)
    val uiState: State<EditBookUiState> = _uiState

    private val _events = MutableSharedFlow<EditBookEvent>()
    val events: SharedFlow<EditBookEvent> = _events.asSharedFlow()

    private var categories: List<String> = emptyList()

    fun loadBookDetails(bookId: String) {
        viewModelScope.launch {
            _uiState.value = EditBookUiState.Loading

            when (val result = bookRepository.getBookById(bookId)) {
                is Resource.Success -> {
                    // If we already have categories, use them, otherwise wait for categories to load
                    if (categories.isNotEmpty()) {
                        _uiState.value = EditBookUiState.BookLoaded(result.data, categories)
                    } else {
                        // Store the book temporarily and wait for categories to be loaded
                        _uiState.value = EditBookUiState.BookLoadedWithoutCategories(result.data)
                    }
                }
                is Resource.Error -> {
                    _uiState.value = EditBookUiState.Error(result.message)
                    _events.emit(EditBookEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = EditBookUiState.Loading
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            when (val result = bookRepository.getAllCategories()) {
                is Resource.Success -> {
                    categories = result.data

                    // If we already have the book loaded, update the state with categories
                    val currentState = _uiState.value
                    if (currentState is EditBookUiState.BookLoadedWithoutCategories) {
                        _uiState.value = EditBookUiState.BookLoaded(currentState.book, categories)
                    }
                }
                is Resource.Error -> {
                    // We can still continue without categories
                    _events.emit(EditBookEvent.ShowError("Failed to load categories: ${result.message}"))
                }
                is Resource.Loading -> {
                    // Do nothing for loading state
                }
            }
        }
    }

    fun updateBook(
        id: String,
        name: String,
        description: String?,
        status: BookStatus?,
        difficultyLevel: DifficultyLevel?,
        category: String?
    ) {
        viewModelScope.launch {
            _uiState.value = EditBookUiState.Loading

            when (val result = bookRepository.updateBook(
                id = id,
                name = name,
                description = description,
                status = status,
                difficultyLevel = difficultyLevel,
                category = category
            )) {
                is Resource.Success -> {
                    _events.emit(EditBookEvent.BookUpdated)
                }
                is Resource.Error -> {
                    // Restore previous state
                    val previousState = _uiState.value
                    if (previousState is EditBookUiState.BookLoaded) {
                        _uiState.value = previousState
                    } else {
                        _uiState.value = EditBookUiState.Error(result.message)
                    }
                    _events.emit(EditBookEvent.ShowError(result.message))
                }
                is Resource.Loading -> {
                    _uiState.value = EditBookUiState.Loading
                }
            }
        }
    }
}

sealed class EditBookUiState {
    object Initial : EditBookUiState()
    object Loading : EditBookUiState()
    data class BookLoaded(val book: Book, val categories: List<String>) : EditBookUiState()
    data class BookLoadedWithoutCategories(val book: Book) : EditBookUiState()
    data class Error(val message: String) : EditBookUiState()
}

sealed class EditBookEvent {
    data class ShowError(val message: String) : EditBookEvent()
    object BookUpdated : EditBookEvent()
    object NavigateBack : EditBookEvent()
}