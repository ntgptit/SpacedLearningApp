package com.spacedlearning.app.presentation.screens.modules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditModuleScreen(
    moduleId: String,
    onNavigateBack: () -> Unit,
    onModuleUpdated: () -> Unit,
    viewModel: EditModuleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var moduleTitle by remember { mutableStateOf("") }
    var wordCount by remember { mutableStateOf("0") }
    var moduleNo by remember { mutableStateOf("1") }

    // Load module details when screen starts
    LaunchedEffect(key1 = moduleId) {
        viewModel.loadModuleDetails(moduleId)
    }

    // Update form fields when module is loaded
    LaunchedEffect(key1 = uiState) {
        if (uiState is EditModuleUiState.ModuleLoaded) {
            val module = (uiState as EditModuleUiState.ModuleLoaded).module
            moduleTitle = module.title
            wordCount = module.wordCount.toString()
            moduleNo = module.moduleNo.toString()
        }
    }

    // Handle events
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is EditModuleEvent.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is EditModuleEvent.ModuleUpdated -> {
                    onModuleUpdated()
                }
                is EditModuleEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Module") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
            if (uiState is EditModuleUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // Module Number
                    OutlinedTextField(
                        value = moduleNo,
                        onValueChange = { moduleNo = it },
                        label = { Text("Module Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        isError = moduleNo.isBlank() || moduleNo.toIntOrNull() == null || moduleNo.toIntOrNull() ?: 0 <= 0,
                        supportingText = {
                            if (moduleNo.isBlank() || moduleNo.toIntOrNull() == null || moduleNo.toIntOrNull() ?: 0 <= 0) {
                                Text("Module number must be a positive number", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Module Title
                    OutlinedTextField(
                        value = moduleTitle,
                        onValueChange = { moduleTitle = it },
                        label = { Text("Module Title") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        isError = moduleTitle.isBlank(),
                        supportingText = {
                            if (moduleTitle.isBlank()) {
                                Text("Module title is required", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Word Count
                    OutlinedTextField(
                        value = wordCount,
                        onValueChange = { wordCount = it },
                        label = { Text("Word Count") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        isError = wordCount.toIntOrNull() == null || wordCount.toIntOrNull() ?: 0 < 0,
                        supportingText = {
                            if (wordCount.toIntOrNull() == null || wordCount.toIntOrNull() ?: 0 < 0) {
                                Text("Word count must be a non-negative number", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (moduleTitle.isBlank()) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Module title is required")
                                }
                                return@Button
                            }

                            val moduleNoInt = moduleNo.toIntOrNull()
                            if (moduleNoInt == null || moduleNoInt <= 0) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Valid module number is required")
                                }
                                return@Button
                            }

                            val wordCountInt = wordCount.toIntOrNull() ?: 0
                            if (wordCountInt < 0) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Word count cannot be negative")
                                }
                                return@Button
                            }

                            viewModel.updateModule(
                                id = moduleId,
                                moduleNo = moduleNoInt,
                                title = moduleTitle,
                                wordCount = wordCountInt
                            )
                        },
                        enabled = uiState !is EditModuleUiState.Loading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState is EditModuleUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}