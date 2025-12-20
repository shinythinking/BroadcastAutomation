package com.shinythinking.broadcastautomation.presentation.broadcast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.ColoredButton
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import com.shinythinking.broadcastautomation.presentation.base.component.SecondaryButton
import com.shinythinking.broadcastautomation.ui.theme.Green500
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun BroadcastScreen(
    onNavigateToMain: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: BroadcastViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                BroadcastEvent.NavigateToMain -> onNavigateToMain()
                is BroadcastEvent.NavigateToSchedule -> { /* TODO: 네비게이션 처리 */
                }

                is BroadcastEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    BroadcastContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onBroadcastImmediately = viewModel::broadcastImmediately,
        onScheduleBroadcast = viewModel::scheduleBroadcast,
        onRetry = viewModel::retry,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun BroadcastContent(
    uiState: BroadcastUiState,
    onBackClick: () -> Unit,
    onBroadcastImmediately: () -> Unit,
    onScheduleBroadcast: () -> Unit,
    onRetry: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "방송 송출",
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is BroadcastUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is BroadcastUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(uiState.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(text = "다시 시도", onClick = onRetry)
                    }
                }

                is BroadcastUiState.Ready -> {
                    BroadcastReadyContent(
                        state = uiState,
                        onBroadcastImmediately = onBroadcastImmediately,
                        onScheduleBroadcast = onScheduleBroadcast,
                        onCancel = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BroadcastReadyContent(
    state: BroadcastUiState.Ready,
    onBroadcastImmediately: () -> Unit,
    onScheduleBroadcast: () -> Unit,
    onCancel: () -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Green500
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "준비 완료!",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "방송을 송출하시겠습니까?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                InfoSummaryCard(
                    generatedAt = state.generatedAt.format(dateFormatter),
                    duration = "${state.estimatedDuration}초"
                )

                Spacer(modifier = Modifier.height(32.dp))

                ColoredButton(
                    text = if (state.isBroadcasting) "송출 중..." else "즉시 송출하기",
                    onClick = onBroadcastImmediately,
                    containerColor = Green500,
                    enabled = !state.isBroadcasting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

                PrimaryButton(
                    text = "예약 송출하기",
                    onClick = onScheduleBroadcast,
                    enabled = !state.isBroadcasting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                SecondaryButton(
                    text = "취소",
                    onClick = onCancel,
                    enabled = !state.isBroadcasting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun InfoSummaryCard(generatedAt: String, duration: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(label = "생성 시간", value = generatedAt)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(label = "예상 재생 시간", value = duration)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}