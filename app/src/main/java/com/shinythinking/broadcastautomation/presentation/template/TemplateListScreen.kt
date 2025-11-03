package com.shinythinking.broadcastautomation.presentation.template

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.broadcastautomation.R
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.EmptyState
import com.shinythinking.broadcastautomation.presentation.base.component.SelectableCard
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme

@Composable
fun TemplateListScreen(
    onBackClick: () -> Unit,
    onTemplateSelect: (String) -> Unit,
//    viewModel: TemplateViewModel = hiltViewModel()
) {
//    val uiState by viewModel.uiState.collectAsState()
    val dummyTemplate = Template(
        id = "1",
        name = "템플릿 1",
        icon = "🎨",
        template = "hello world",
        fields = listOf()
    )
    val dummyUiState = TemplateUiState.Success(listOf(dummyTemplate, dummyTemplate, dummyTemplate))

    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = stringResource(R.string.select_template),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (dummyUiState) {
            is TemplateUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TemplateUiState.Success -> {
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

                    items(dummyUiState.templates) { template ->
                        SelectableCard(
                            title = template.name,
                            icon = template.icon,
                            onClick = { onTemplateSelect(template.id) }
                        )
                    }
                }
            }

            is TemplateUiState.Error -> {
                EmptyState(
                    icon = "⚠️",
                    title = stringResource(R.string.error),
                    description = dummyUiState.message,
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
    BroadcastAutomationTheme {
        TemplateListScreen(
            onBackClick = {},
            onTemplateSelect = {}
        )
    }
}