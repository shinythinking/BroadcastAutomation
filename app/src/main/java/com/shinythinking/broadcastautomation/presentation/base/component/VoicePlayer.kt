package com.shinythinking.broadcastautomation.presentation.base.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.broadcastautomation.ui.theme.BroadcastAutomationTheme

@Composable
fun VoicePlayer(
    isPlaying: Boolean,
    progress: Float,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        FilledIconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(72.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Build  else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "일시정지" else "재생",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun VoicePlayerPreview() {
    var flag = true
    BroadcastAutomationTheme {
        VoicePlayer(
            isPlaying = flag,
            progress = 0.5f,
            onPlayPauseClick = {flag = !flag}
        )
    }
}