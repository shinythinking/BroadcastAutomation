package com.shinythinking.broadcastautomation.presentation.tts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.domain.model.VoiceGender
import com.shinythinking.broadcastautomation.domain.model.VoiceSpeed
import com.shinythinking.broadcastautomation.domain.model.VoiceVolume
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastDropdown
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TTSSettingsScreen(
    scriptId: String,
    onBackClick: () -> Unit,
    onVoiceGenerated: (String) -> Unit,
    viewModel: TTSViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is TTSEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is TTSEvent.NavigateToPreview -> {
                    onVoiceGenerated(event.scriptId)
                }
            }
        }
    }

    val isGenerating = uiState is TTSUiState.Generating
    val settings = when (val state = uiState) {
        is TTSUiState.Configuring -> state.settings
        is TTSUiState.Generating -> state.settings
    }

    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "음성 설정",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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
                    Text(
                        text = "성별 선택",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VoiceGender.entries.forEach { gender ->
                            FilterChip(
                                selected = settings.voiceGender == gender,
                                onClick = { if (!isGenerating) viewModel.updateGender(gender) },
                                label = { Text(gender.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    BroadcastDropdown(
                        value = settings.volume.displayName,
                        onValueChange = { displayName ->
                            if (!isGenerating) {
                                VoiceVolume.entries.find { it.displayName == displayName }?.let {
                                    viewModel.updateVolume(it)
                                }
                            }
                        },
                        label = "볼륨",
                        options = VoiceVolume.entries.map { it.displayName },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    BroadcastDropdown(
                        value = settings.speed.displayName,
                        onValueChange = { displayName ->
                            if (!isGenerating) {
                                VoiceSpeed.entries.find { it.displayName == displayName }?.let {
                                    viewModel.updateSpeed(it)
                                }
                            }
                        },
                        label = "속도",
                        options = VoiceSpeed.entries.map { it.displayName }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "음성 생성하기",
                onClick = { viewModel.generateVoice() },
                enabled = !isGenerating
            )

            if (isGenerating) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}