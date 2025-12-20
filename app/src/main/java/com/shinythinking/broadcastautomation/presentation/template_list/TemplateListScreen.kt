package com.shinythinking.broadcastautomation.presentation.template_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.broadcastautomation.R
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.EmptyState
import com.shinythinking.broadcastautomation.presentation.base.component.LoadingOverlay
import com.shinythinking.broadcastautomation.presentation.base.component.SelectableCard
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme

@Composable
fun TemplateListScreen(
    onBackClick: () -> Unit,
    onTemplateSelect: (String) -> Unit,
    viewModel: TemplateListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TemplateListContent(
        uiState = uiState,
        onTemplateSelect = onTemplateSelect,
        onBackClick = onBackClick
    )
}

@Composable
fun TemplateListContent(
    uiState: TemplateListUiState,
    onTemplateSelect: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = stringResource(R.string.select_template),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        val state = uiState
        when (state) {
            is TemplateListUiState.Loading -> {
                LoadingOverlay(
                    isLoading = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    message = "잠시만 기다려주세요~"
                )
            }

            is TemplateListUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.plz_select_template),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(state.templates) { template ->
                        SelectableCard(
                            title = template.name,
                            icon = template.icon,
                            onClick = { onTemplateSelect(template.id) }
                        )
                    }
                }
            }

            is TemplateListUiState.Error -> {
                EmptyState(
                    icon = "⚠️",
                    title = stringResource(R.string.error),
                    description = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TemplateListScreenPreview() {
    val templates = listOf(
        Template(
            id = "1",
            name = "마을 회의 공지",
            icon = "📋",
            template = "주민 여러분께 알립니다.",
            fields = emptyList()
        ),
        Template(
            id = "2",
            name = "결혼식 안내",
            icon = "💒",
            template = "경사스러운 소식을 전해드립니다.",
            fields = emptyList()
        ),
        Template(
            id = "3",
            name = "긴급 재난 안내",
            icon = "⚠️",
            template = "긴급 안내 방송입니다.",
            fields = emptyList()
        )
    )

    BroadcastAutomationTheme {
        TemplateListContent(
            onBackClick = {},
            onTemplateSelect = {},
            uiState = TemplateListUiState.Success(
                templates = templates
            )
        )
    }
}