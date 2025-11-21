package com.shinythinking.broadcastautomation.presentation.script_edit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
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
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastMultilineTextField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import com.shinythinking.broadcastautomation.presentation.base.component.SecondaryButton
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime

@Composable
fun ScriptEditScreen(
    scriptId: String?,
    onBackClick: () -> Unit,
    onConvertToVoice: (String) -> Unit,
    viewModel: ScriptEditViewmodel = hiltViewModel()
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
        updateContent = viewModel::updateContent,
        saveAndContinue = viewModel::saveAndContinue,
        retry = viewModel::retry
    )
}

@Composable
fun ScriptEditContent(
    uiState: ScriptEditUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    updateContent: (String) -> Unit,
    saveAndContinue: () -> Unit,
    retry: () -> Unit
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = stringResource(R.string.script_confirm),
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

            is ScriptEditUiState.Success -> {
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
                                label = stringResource(R.string.broadcast_script),
                                placeholder = stringResource(R.string.input_script),
                                minHeight = 200
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = stringResource(R.string.convert_to_voice),
                        onClick = { saveAndContinue() },
                        enabled = state.isValid && !state.isSaving,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    SecondaryButton(
                        text = stringResource(R.string.regeneration),
                        onClick = { /* TODO: Implement regeneration */ },
                        enabled = !state.isSaving
                    )

                    if (state.isSaving) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            is ScriptEditUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "⚠️", style = MaterialTheme.typography.displayLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "오류 발생", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButton(
                        text = "다시 시도",
                        onClick = { retry() }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScriptEditSuccessPreview() {
    val uiState = ScriptEditUiState.Success(
        script = Script(
            id = "1",
            title = "Sample Script",
            content = "This is a sample script content.",
            createdAt = LocalDateTime.now(),
            templateId = "1"
        ),
        content = "This is a sample script content.",
        isSaving = false
    )
    BroadcastAutomationTheme {
        ScriptEditContent(
            uiState = uiState,
            snackbarHostState = SnackbarHostState(),
            onBackClick = { },
            updateContent = { },
            saveAndContinue = { },
            retry = { }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScriptEditErrorPreview() {
    val uiState = ScriptEditUiState.Error(
        message = "An error occurred while loading the script.",
    )
    BroadcastAutomationTheme {
        ScriptEditContent(
            uiState = uiState,
            snackbarHostState = SnackbarHostState(),
            onBackClick = { },
            updateContent = { },
            saveAndContinue = { },
            retry = { }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScriptEditLoadingPreview() {
    val uiState = ScriptEditUiState.Loading
    BroadcastAutomationTheme {
        ScriptEditContent(
            uiState = uiState,
            snackbarHostState = SnackbarHostState(),
            onBackClick = { },
            updateContent = { },
            saveAndContinue = { },
            retry = { }
        )
    }
}