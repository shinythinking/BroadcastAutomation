package com.shinythinking.broadcastautomation.presentation.template_edit

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.SnackbarDuration
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
import com.shinythinking.broadcastautomation.domain.model.FieldType
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.domain.model.TemplateField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastDateField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTextField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTimeField
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.LoadingOverlay
import com.shinythinking.broadcastautomation.presentation.base.component.PrimaryButton
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TemplateEditScreen(
    onBackClick: () -> Unit,
    onScriptGenerated: (String) -> Unit,
    viewModel: TemplateEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is TemplateEditEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }

                is TemplateEditEvent.NavigateToScript -> {
                    onScriptGenerated(event.scriptId)
                }
            }
        }
    }

    TemplateEditContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetryClick = viewModel::retry,
        onFieldValueChange = viewModel::updateFieldValue,
        onSaveClick = viewModel::saveAndContinue,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun TemplateEditContent(
    uiState: TemplateEditUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onFieldValueChange: (String, String) -> Unit,
    onSaveClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = when (uiState) {
                    is TemplateEditUiState.Success -> uiState.template.name
                    else -> "템플릿 편집"
                },
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (uiState) {
            is TemplateEditUiState.Loading -> {
                LoadingOverlay(
                    isLoading = true,
                    message = "템플릿 정보를 불러오는 중입니다...",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is TemplateEditUiState.Success -> {
                SuccessContent(
                    state = uiState,
                    onFieldValueChange = onFieldValueChange,
                    onSaveClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is TemplateEditUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetryClick = onRetryClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun SuccessContent(
    state: TemplateEditUiState.Success,
    onFieldValueChange: (String, String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                state.template.fields.forEach { field ->
                    when (field.type) {
                        FieldType.DATE -> {
                            BroadcastDateField(
                                value = state.fieldValues[field.id] ?: "",
                                onValueChange = { onFieldValueChange(field.id, it) },
                                label = field.name,
                                placeholder = field.placeholder,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        FieldType.TIME -> {
                            BroadcastTimeField(
                                value = state.fieldValues[field.id] ?: "",
                                onValueChange = { onFieldValueChange(field.id, it) },
                                label = field.name,
                                placeholder = field.placeholder,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        else -> {
                            BroadcastTextField(
                                value = state.fieldValues[field.id] ?: "",
                                onValueChange = { onFieldValueChange(field.id, it) },
                                label = field.name,
                                placeholder = field.placeholder,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.preview),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.generatedScript,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "대본 생성하기",
            onClick = onSaveClick,
            enabled = !state.isGenerating
        )

        if (state.isGenerating) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.error_happen),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "다시 시도",
            onClick = onRetryClick
        )
    }
}

@Preview(
    name = "템플릿 편집 - 로딩",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TemplateEditLoadingPreview() {
    BroadcastAutomationTheme {
        TemplateEditContent(
            uiState = TemplateEditUiState.Loading,
            onBackClick = {},
            onFieldValueChange = { _, _ -> },
            onSaveClick = {},
            onRetryClick = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Preview(
    name = "템플릿 편집 - 편집 중",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TemplateEditEditingPreview() {
    val template = Template(
        id = "template_meeting",
        name = "마을 회의 공지",
        icon = "📋",
        template = "주민 여러분께 알립니다. [날짜] [시간]에 [장소]에서 마을 회의가 있을 예정입니다.",
        fields = listOf(
            TemplateField("날짜", "회의 날짜", FieldType.DATE, "2025-10-13"),
            TemplateField("시간", "회의 시간", FieldType.TIME, "14:00"),
            TemplateField("장소", "장소", FieldType.LOCATION, "마을회관")
        )
    )

    val fieldValues = mapOf(
        "날짜" to "2025-10-13",
        "시간" to "14:00",
        "장소" to "마을회관"
    )

    val preview = "주민 여러분께 알립니다. 2025-10-13 14:00에 마을회관에서 마을 회의가 있을 예정입니다."

    BroadcastAutomationTheme {
        TemplateEditContent(
            uiState = TemplateEditUiState.Success(
                template = template,
                fieldValues = fieldValues,
                generatedScript = preview,
                isGenerating = false
            ),
            onBackClick = {},
            onFieldValueChange = { _, _ -> },
            onSaveClick = {},
            onRetryClick = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Preview(
    name = "템플릿 편집 - 생성 중",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TemplateEditGeneratingPreview() {
    val template = Template(
        id = "template_meeting",
        name = "마을 회의 공지",
        icon = "📋",
        template = "주민 여러분께 알립니다. [날짜] [시간]에 [장소]에서 마을 회의가 있을 예정입니다.",
        fields = listOf(
            TemplateField("날짜", "회의 날짜", FieldType.DATE, ""),
            TemplateField("시간", "회의 시간", FieldType.TIME, ""),
            TemplateField("장소", "장소", FieldType.LOCATION, "")
        )
    )

    BroadcastAutomationTheme {
        TemplateEditContent(
            uiState = TemplateEditUiState.Success(
                template = template,
                fieldValues = mapOf("날짜" to "", "시간" to "", "장소" to ""),
                generatedScript = "",
                isGenerating = true
            ),
            onBackClick = {},
            onFieldValueChange = { _, _ -> },
            onSaveClick = {},
            snackbarHostState = SnackbarHostState(),
            onRetryClick = {}
        )
    }
}

@Preview(
    name = "템플릿 편집 - 에러",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TemplateEditErrorPreview() {
    BroadcastAutomationTheme {
        TemplateEditContent(
            uiState = TemplateEditUiState.Error("템플릿을 찾을 수 없습니다"),
            onBackClick = {},
            onFieldValueChange = { _, _ -> },
            onSaveClick = {},
            snackbarHostState = SnackbarHostState(),
            onRetryClick = {}
        )
    }
}