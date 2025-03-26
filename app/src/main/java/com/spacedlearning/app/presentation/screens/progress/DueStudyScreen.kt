package com.spacedlearning.app.presentation.screens.progress

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.spacedlearning.app.domain.model.ModuleProgress
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueStudyScreen(
    onProgressClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ProgressViewModel
) {
    val dueProgressItems = viewModel.dueProgress.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Handle events
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ProgressEvent.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is ProgressEvent.RepetitionCompleted -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Repetition marked as completed!")
                        dueProgressItems.refresh()
                    }
                }
                is ProgressEvent.RepetitionSkipped -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Repetition skipped. It will appear tomorrow.")
                        dueProgressItems.refresh()
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Study Queue") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                dueProgressItems.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                dueProgressItems.loadState.refresh is LoadState.Error -> {
                    val error = dueProgressItems.loadState.refresh as LoadState.Error
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${error.error.localizedMessage ?: "An error occurred"}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                dueProgressItems.itemCount == 0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You're all caught up!",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "There are no modules due for review today.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(64.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Today's date header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Today: ${LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Text(
                            text = "${dueProgressItems.itemCount} modules due for review",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(count = dueProgressItems.itemCount) { index ->
                                dueProgressItems[index]?.let { progress ->
                                    DueStudyItem(
                                        progress = progress,
                                        onStartClick = { onProgressClick(progress.id.toString()) },
                                        onComplete = {
                                            // In a real app, we'd handle the specific repetition ID
                                            // For simplicity, we're just using a placeholder
                                            if (progress.repetitions.isNotEmpty()) {
                                                val firstDueRepetition = progress.repetitions.firstOrNull {
                                                    it.status.name == "NOT_STARTED" &&
                                                            (it.reviewDate?.isBefore(LocalDate.now()) == true ||
                                                                    it.reviewDate?.isEqual(LocalDate.now()) == true)
                                                }

                                                firstDueRepetition?.let {
                                                    viewModel.completeRepetition(it.id.toString())
                                                }
                                            }
                                        },
                                        onSkip = {
                                            // Similar to onComplete
                                            if (progress.repetitions.isNotEmpty()) {
                                                val firstDueRepetition = progress.repetitions.firstOrNull {
                                                    it.status.name == "NOT_STARTED" &&
                                                            (it.reviewDate?.isBefore(LocalDate.now()) == true ||
                                                                    it.reviewDate?.isEqual(LocalDate.now()) == true)
                                                }

                                                firstDueRepetition?.let {
                                                    viewModel.skipRepetition(it.id.toString())
                                                }
                                            }
                                        }
                                    )
                                }
                            }

                            if (dueProgressItems.loadState.append is LoadState.Loading) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueStudyItem(
    progress: ModuleProgress,
    onStartClick: () -> Unit,
    onComplete: () -> Unit,
    onSkip: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = progress.moduleTitle ?: "Unknown Module",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Current cycle: ${progress.cyclesStudied.name.replace('_', ' ')}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start")
                }

                OutlinedButton(
                    onClick = onComplete,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Mark Done")
                }

                OutlinedButton(
                    onClick = onSkip
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        }
    }
}