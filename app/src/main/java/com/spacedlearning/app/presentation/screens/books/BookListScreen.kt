package com.spacedlearning.app.presentation.screens.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.spacedlearning.app.domain.model.Book
import com.spacedlearning.app.domain.model.BookStatus
import com.spacedlearning.app.domain.model.DifficultyLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: BookViewModel
) {
    val books = viewModel.books.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search functionality */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                books.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                books.loadState.refresh is LoadState.Error -> {
                    val error = books.loadState.refresh as LoadState.Error
                    Text(
                        text = error.error.localizedMessage ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(count = books.itemCount) { index ->
                            books[index]?.let { book ->
                                BookItem(
                                    book = book,
                                    onClick = { onBookClick(book.id.toString()) }
                                )
                            }
                        }

                        if (books.loadState.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        BookFilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { status, difficulty, category ->
                viewModel.applyFilters(status, difficulty, category)
                showFilterDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = book.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                BookStatusChip(status = book.status)

                Spacer(modifier = Modifier.width(8.dp))

                book.difficultyLevel?.let { difficulty ->
                    BookDifficultyChip(difficulty = difficulty)
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${book.moduleCount} modules",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun BookStatusChip(
    status: BookStatus
) {
    Surface(
        color = when (status) {
            BookStatus.PUBLISHED -> MaterialTheme.colorScheme.primaryContainer
            BookStatus.DRAFT -> MaterialTheme.colorScheme.secondaryContainer
            BookStatus.ARCHIVED -> MaterialTheme.colorScheme.tertiaryContainer
        },
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = when (status) {
                BookStatus.PUBLISHED -> MaterialTheme.colorScheme.onPrimaryContainer
                BookStatus.DRAFT -> MaterialTheme.colorScheme.onSecondaryContainer
                BookStatus.ARCHIVED -> MaterialTheme.colorScheme.onTertiaryContainer
            }
        )
    }
}

@Composable
fun BookDifficultyChip(
    difficulty: DifficultyLevel
) {
    Surface(
        color = when (difficulty) {
            DifficultyLevel.BEGINNER -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            DifficultyLevel.INTERMEDIATE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            DifficultyLevel.ADVANCED -> MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            DifficultyLevel.EXPERT -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        },
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = difficulty.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFilterDialog(
    onDismiss: () -> Unit,
    onApplyFilters: (BookStatus?, DifficultyLevel?, String?) -> Unit
) {
    var selectedStatus by remember { mutableStateOf<BookStatus?>(null) }
    var selectedDifficulty by remember { mutableStateOf<DifficultyLevel?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Filter dialog implementation...
    // Would include filter options for status, difficulty level, and category

    // For simplicity, we're not implementing the full dialog here
}