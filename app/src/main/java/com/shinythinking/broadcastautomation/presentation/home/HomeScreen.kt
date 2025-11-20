package com.shinythinking.broadcastautomation.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.broadcastautomation.R
import com.shinythinking.broadcastautomation.presentation.base.component.BroadcastTopBar
import com.shinythinking.broadcastautomation.presentation.base.component.FeatureCard
import com.shinythinking.broadcastautomation.presentation.base.component.FeatureCardWithPainter
import com.shinythinking.broadcastautomation.presentation.base.component.InfoCard
import com.shinythinking.broadcastautomation.ui.theme.Blue500
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme
import com.shinythinking.broadcastautomation.ui.theme.Green500
import com.shinythinking.broadcastautomation.ui.theme.Purple500

@Composable
fun HomeScreen(
    onNavigateToTemplate: () -> Unit,
    onNavigateToAI: () -> Unit,
    onNavigateToArchive: () -> Unit
) {
    Scaffold(
        topBar = {
            BroadcastTopBar(
                title = stringResource(R.string.app_title),
                showMenuButton = false,
            )
        }
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
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.make_broadcast),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureCard(
                        title = stringResource(R.string.script_by_template),
                        description = stringResource(R.string.by_template),
                        icon = Icons.Default.Create,
                        iconColor = Blue500,
                        borderColor = Blue500,
                        onClick = onNavigateToTemplate,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    FeatureCardWithPainter(
                        title = stringResource(R.string.by_ai),
                        description = stringResource(R.string.script_by_ai),
                        icon = painterResource(R.drawable.ic_chat_ai),
                        iconColor = Purple500,
                        borderColor = Purple500,
                        onClick = onNavigateToAI,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    FeatureCardWithPainter(
                        title = stringResource(R.string.refer_archive),
                        description = stringResource(R.string.load_archive),
                        icon = painterResource(R.drawable.ic_history_book),
                        iconColor = Green500,
                        borderColor = Green500,
                        onClick = onNavigateToArchive
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                message = stringResource(R.string.info),
                icon = "💡"
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HomeScreenPreview() {
    BroadcastAutomationTheme {
        HomeScreen(
            onNavigateToTemplate = {},
            onNavigateToAI = {},
            onNavigateToArchive = {}
        )
    }
}