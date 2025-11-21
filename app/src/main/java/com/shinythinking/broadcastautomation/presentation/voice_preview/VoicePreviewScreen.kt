package com.shinythinking.broadcastautomation.presentation.voice_preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.R
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import com.shinythinking.broadcastautomation.presentation.base.component.SecondaryButton
import com.shinythinking.broadcastautomation.presentation.base.component.VoicePlayer
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme

@Composable
fun VoicePreviewScreen(
    scriptId: String,
    onBackClick: () -> Unit,
    onBroadcast: (String) -> Unit,
    viewModel: VoicePreviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val isPlaying = uiState is VoicePreviewUiState.Playing
    val progress = when (val state = uiState) {
        is VoicePreviewUiState.Idle -> state.progress
        is VoicePreviewUiState.Playing -> state.progress
        is VoicePreviewUiState.Paused -> state.progress
        is VoicePreviewUiState.Completed -> 1f
    }

    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "음성 미리듣기",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_voice_preview),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "생성된 음성을 미리 들어보세요",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    VoicePlayer(
                        isPlaying = isPlaying,
                        progress = progress,
                        onPlayPauseClick = { viewModel.togglePlayPause() }
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "방송 송출하기",
                    onClick = { onBroadcast(scriptId) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                SecondaryButton(
                    text = "다시 생성하기",
                    onClick = {
                        viewModel.reset()
                        onBackClick()
                    }
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun VoicePreviewScreenPreview() {
    BroadcastAutomationTheme {
        VoicePreviewScreen(
            scriptId = "sampleScriptId",
            onBackClick = {},
            onBroadcast = {},
        )
    }
}