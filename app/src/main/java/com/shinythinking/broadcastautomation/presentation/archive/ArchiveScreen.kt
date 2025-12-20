package com.shinythinking.broadcastautomation.presentation.archive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.presentation.base.component.ArchiveItem
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.EmptyState
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    onBackClick: () -> Unit,
    onScriptSelect: (String) -> Unit,
    viewModel: ArchiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ArchiveEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    ArchiveContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onScriptClick = onScriptSelect,
        onDeleteScript = viewModel::deleteScript,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissItem(
    script: Script,
    onScriptClick: (String) -> Unit,
    onDelete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    coroutineScope.launch {
                        onDelete()
                    }
                    true
                }

                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            DismissBackground(dismissState)
        }
    ) {
        ArchiveItem(
            script = script,
            onClick = { onScriptClick(script.id) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "삭제",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveContent(
    uiState: ArchiveUiState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onScriptClick: (String) -> Unit,
    onDeleteScript: (String) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = "이전 방송 목록",
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
        ) {
            when (uiState) {
                is ArchiveUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ArchiveUiState.Success -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text("키워드로 검색하기...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "검색"
                                )
                            },
                            singleLine = true
                        )
                    }

                    when {
                        uiState.isEmpty && uiState.isSearching -> {
                            EmptyState(
                                icon = "🔍",
                                title = "검색 결과 없음",
                                description = "검색어와 일치하는 방송을 찾을 수 없습니다.",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        uiState.isEmpty -> {
                            EmptyState(
                                icon = "📻",
                                title = "방송 기록이 없습니다",
                                description = "첫 방송을 만들어보세요!",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = uiState.scripts,
                                    key = { it.id }
                                ) { script ->
                                    SwipeToDismissItem(
                                        script = script,
                                        onScriptClick = onScriptClick,
                                        onDelete = { onDeleteScript(script.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                is ArchiveUiState.Error -> {
                    EmptyState(
                        icon = "⚠️",
                        title = "오류 발생",
                        description = uiState.message,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(name = "Archive Item", showBackground = true)
@Composable
fun ArchiveItemPreview() {
    BroadcastAutomationTheme {
        ArchiveItem(
            script = Script(
                id = "1",
                title = "마을 회의 안내",
                content = "주민 여러분께 알립니다. 2025-10-13 14:00에 마을회관에서 마을 회의가 있을 예정입니다. 많은 참석 부탁드립니다.",
                createdAt = LocalDateTime.of(2025, 10, 10, 14, 30),
                templateId = ""
            ),
            onClick = {}
        )
    }
}

@Preview(name = "Archive Item - Long Content", showBackground = true)
@Composable
fun ArchiveItemLongPreview() {
    BroadcastAutomationTheme {
        ArchiveItem(
            script = Script(
                id = "2",
                title = "긴급 재난 안내",
                content = "긴급 안내 방송입니다. 폭설로 인해 마을 일대에 차량 통행 제한이 시행됩니다. 주민 여러분께서는 안전에 유의해 주시기 바랍니다. 자세한 사항은 마을 사무소로 문의해 주세요. 긴급 안내 방송입니다. 폭설로 인해 마을 일대에 차량 통행 제한이 시행됩니다.",
                createdAt = LocalDateTime.of(2025, 10, 5, 8, 15),
                templateId = ""
            ),
            onClick = {}
        )
    }
}