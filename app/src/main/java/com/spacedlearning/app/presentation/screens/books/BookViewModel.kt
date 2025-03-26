package com.spacedlearning.app.presentation.screens.books

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.spacedlearning.app.domain.model.Book
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel
import com.spacedlearning.app.domain.repository.BookRepository
import com.spacedlearning.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _bookDetailState = MutableStateFlow<BookDetailState>(BookDetailState.Loading)
    val bookDetailState: StateFlow<BookDetailState> = _bookDetailState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _events = MutableSharedFlow<BookEvent>()
    val events: SharedFlow<BookEvent> = _events.asSharedFlow()

    // Current filter states
    private val _currentStatus = MutableStateFlow<BookStatus?>(null)
    private val _currentDifficulty = MutableStateFlow<DifficultyLevel?>(null)
    private val _currentCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow<String>("")

    // Filtered books flow
    val books: Flow<PagingData<Book>> = bookRepository.getAllBooks()
        .cachedIn(viewModelScope)

    init {
        loadCategories()
    }

    fun loadBookDetail(bookId: String) {
        viewModelScope.launch {
            _bookDetailState.value = BookDetailState.Loading

            when (val result = bookRepository.getBookById(bookId)) {
                is Resource.Success -> {
                    _bookDetailState.value = BookDetailState.Success(result.data)
                }
                is Resource.Error -> {
                    _bookDetailState.value = BookDetailState.Error(result.message)
                }
                is Resource.Loading -> {
                    _bookDetailState.value = BookDetailState.Loading
                }
            }
        }
    }

    fun searchBooks(query: String) {
        _searchQuery.value = query
    }

    fun applyFilters(status: BookStatus?, difficultyLevel: DifficultyLevel?, category: String?) {
        _currentStatus.value = status
        _currentDifficulty.value = difficultyLevel
        _currentCategory.value = category
    }

    fun resetFilters() {
        _currentStatus.value = null
        _currentDifficulty.value = null
        _currentCategory.value = null
        _searchQuery.value = ""
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = bookRepository.getAllCategories()) {
                is Resource.Success -> {
                    _categories.value = result.data
                }
                is Resource.Error -> {
                    // Handle error
                }
                is Resource.Loading -> {
                    // Handle loading
                }
            }
        }
    }
}

sealed class BookDetailState {
    object Loading : BookDetailState()
    data class Success(val book: Book) : BookDetailState()
    data class Error(val message: String) : BookDetailState()
}

sealed class BookEvent {
    data class ShowError(val message: String) : BookEvent()
    object NavigateBack : BookEvent()
}