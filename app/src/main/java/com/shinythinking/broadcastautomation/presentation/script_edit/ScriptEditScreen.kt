package com.shinythinking.broadcastautomation.presentation.script_edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.R
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastMultilineTextField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.EmptyState
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScriptEditScreen(
    onBackClick: () -> Unit,
    onConvertToVoice: (String) -> Unit,
    viewModel: ScriptEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ScriptEditEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is ScriptEditEvent.NavigateToTTS -> {
                    onConvertToVoice(event.scriptId)
                }
            }
        }
    }

    ScriptEditContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        saveAndConvertToVoice = { viewModel.saveAndContinue() },
        updateContent = { viewModel.updateContent(it) },
        onRetryClick = { viewModel.retry() }
    )
}

@Composable
fun ScriptEditContent(
    uiState: ScriptEditUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    saveAndConvertToVoice: () -> Unit,
    updateContent: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "대본 확인",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (val state = uiState) {
            is ScriptEditUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ScriptEditUiState.Editing -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            BroadcastMultilineTextField(
                                value = state.content,
                                onValueChange = { if (!state.isSaving) updateContent(it) },
                                label = "방송 대본",
                                placeholder = "방송 내용을 입력하세요",
                                minHeight = 200
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "음성으로 변환하기",
                        onClick = { saveAndConvertToVoice() },
                        enabled = state.isValid && !state.isSaving,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (state.isSaving) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            is ScriptEditUiState.Error -> {
                EmptyState(
                    icon = "⚠️",
                    title = stringResource(R.string.error),
                    description = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    retryEnable = true,
                    retryAction = { onRetryClick }
                )

            }
        }
    }
}

@Preview
@Composable
fun ScriptEditPreview() {
    BroadcastAutomationTheme {
        ScriptEditContent(
            onBackClick = {},
            uiState = ScriptEditUiState.Error("error"),
            snackbarHostState = SnackbarHostState(),
            saveAndConvertToVoice = {},
            updateContent = {},
            onRetryClick = {}
        )
    }
}