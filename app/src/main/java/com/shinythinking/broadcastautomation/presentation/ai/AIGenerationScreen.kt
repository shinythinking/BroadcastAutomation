package com.shinythinking.broadcastautomation.presentation.ai

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.domain.model.AILength
import com.shinythinking.broadcastautomation.domain.model.AITone
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastDropdown
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastMultilineTextField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.ColoredButton
import com.shinythinking.broadcastautomation.ui.theme.Purple500
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AIGenerationScreen(
    onBackClick: () -> Unit,
    onScriptGenerated: (String) -> Unit,
    viewModel: AIGenerationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AIGenerationEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is AIGenerationEvent.NavigateToScript -> {
                    onScriptGenerated(event.scriptId)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "AI 대본 생성",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val isGenerating = uiState is AIGenerationUiState.Generating
        val keywords = when (val state = uiState) {
            is AIGenerationUiState.Editing -> state.keywords
            is AIGenerationUiState.Generating -> state.keywords
        }
        val options = when (val state = uiState) {
            is AIGenerationUiState.Editing -> state.options
            is AIGenerationUiState.Generating -> state.options
        }

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
                        value = keywords,
                        onValueChange = { if (!isGenerating) viewModel.updateKeywords(it) },
                        label = "핵심 내용 입력",
                        placeholder = "예: 내일 새벽 폭설. 차량 운행 자제. 비닐하우스 눈 치우기.",
                        minHeight = 120
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    BroadcastDropdown(
                        value = options.tone.displayName,
                        onValueChange = { displayName ->
                            if (!isGenerating) {
                                AITone.entries.find { it.displayName == displayName }?.let {
                                    viewModel.updateTone(it)
                                }
                            }
                        },
                        label = "어조 선택",
                        options = AITone.entries.map { it.displayName },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    BroadcastDropdown(
                        value = "${options.length.displayName} (${options.length.description})",
                        onValueChange = { displayName ->
                            if (!isGenerating) {
                                val name = displayName.substringBefore(" (")
                                AILength.entries.find { it.displayName == name }?.let {
                                    viewModel.updateLength(it)
                                }
                            }
                        },
                        label = "길이 선택",
                        options = AILength.entries.map { "${it.displayName} (${it.description})" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ColoredButton(
                text = "AI 대본 생성하기",
                onClick = { viewModel.generateScript() },
                containerColor = Purple500,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                enabled = !isGenerating && keywords.isNotBlank()
            )

            if (isGenerating) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}