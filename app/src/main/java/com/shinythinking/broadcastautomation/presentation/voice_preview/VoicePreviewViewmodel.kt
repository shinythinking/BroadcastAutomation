package com.shinythinking.broadcastautomation.presentation.voice_preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoicePreviewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<VoicePreviewUiState>(
        VoicePreviewUiState.Idle()
    )
    val uiState: StateFlow<VoicePreviewUiState> = _uiState.asStateFlow()

    private var playbackJob: Job? = null

    fun togglePlayPause() {
        when (val state = _uiState.value) {
            is VoicePreviewUiState.Idle -> play(0f)
            is VoicePreviewUiState.Playing -> pause(state.progress)
            is VoicePreviewUiState.Paused -> play(state.progress)
            is VoicePreviewUiState.Completed -> play(0f)
        }
    }

    private fun play(startProgress: Float) {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            var progress = startProgress
            while (progress < 1f) {
                _uiState.value = VoicePreviewUiState.Playing(progress)
                delay(100)
                progress = (progress + 0.02f).coerceAtMost(1f)
            }
            _uiState.value = VoicePreviewUiState.Completed()
        }
    }

    private fun pause(currentProgress: Float) {
        playbackJob?.cancel()
        _uiState.value = VoicePreviewUiState.Paused(currentProgress)
    }

    fun reset() {
        playbackJob?.cancel()
        _uiState.value = VoicePreviewUiState.Idle()
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
    }
}