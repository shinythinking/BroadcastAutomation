package com.shinythinking.broadcastautomation.presentation.broadcast

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shinythinking.broadcastautomation.domain.model.BroadcastMessage
import com.shinythinking.broadcastautomation.domain.model.BroadcastSchedule
import com.shinythinking.broadcastautomation.domain.model.VoiceSettings
import com.shinythinking.broadcastautomation.domain.repository.BroadcastRepository
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import com.shinythinking.broadcastautomation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val localDataRepository: LocalDataRepository,
    private val broadcastRepository: BroadcastRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Screen.Broadcast>()
    private val scriptId: String = args.scriptId

    private val _uiState = MutableStateFlow<BroadcastUiState>(BroadcastUiState.Loading)
    val uiState: StateFlow<BroadcastUiState> = _uiState.asStateFlow()

    private val _events = Channel<BroadcastEvent>(Channel.BUFFERED)
    val events: Flow<BroadcastEvent> = _events.receiveAsFlow()

    init {
        loadScript()
    }

    private fun loadScript() {
        viewModelScope.launch {
            try {
                val script = localDataRepository.getScript(scriptId)

                if (script == null) {
                    _uiState.value = BroadcastUiState.Error(
                        message = "방송 대본을 찾을 수 없습니다"
                    )
                    return@launch
                }

                val estimatedDuration = calculateEstimatedDuration(script.content)

                _uiState.value = BroadcastUiState.Ready(
                    script = script,
                    generatedAt = LocalDateTime.now(),
                    estimatedDuration = estimatedDuration
                )
                Log.d("BroadcastViewModel", "Script loaded: ${script.content}")
            } catch (e: Exception) {
                _uiState.value = BroadcastUiState.Error(
                    message = "대본 로딩 중 오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }

    fun broadcastImmediately() {
        val currentState = _uiState.value
        if (currentState !is BroadcastUiState.Ready || currentState.isBroadcasting) return
        Log.d("BroadcastViewModel", "Broadcasting immediately")

        _uiState.value = currentState.copy(isBroadcasting = true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val message = BroadcastMessage(
                    text = currentState.script.content,
                    voiceSettings = VoiceSettings(),
                    schedule = BroadcastSchedule.Immediate
                )

                broadcastRepository.sendBroadcast(message)
                    .onSuccess {
                        Log.d("BroadcastViewModel", "Broadcast successful")
                        _events.send(
                            element = BroadcastEvent.ShowSnackbar("방송을 성공적으로 송출했습니다.")
                        )
                        _events.send(BroadcastEvent.NavigateToMain)
                    }
                    .onFailure { e ->
                        _uiState.value = currentState.copy(isBroadcasting = false)

                        _events.send(
                            BroadcastEvent.ShowSnackbar(
                                "방송 송출 실패: ${e.message}"
                            )
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = currentState.copy(isBroadcasting = false)

                Log.e("BroadcastViewModel", "Exception during broadcast: ${e.message}")
                _events.send(
                    BroadcastEvent.ShowSnackbar(
                        "방송 송출 중 오류가 발생했습니다."
                    )
                )
            }
        }
    }

    fun scheduleBroadcast() {
        val currentState = _uiState.value
        if (currentState !is BroadcastUiState.Ready) return

        viewModelScope.launch {
            _events.send(BroadcastEvent.NavigateToSchedule(scriptId))
        }
    }

    fun retry() {
        _uiState.value = BroadcastUiState.Loading
        loadScript()
    }

    private fun calculateEstimatedDuration(content: String): Int {
        val charsPerMinute = 200
        val totalChars = content.length
        val minutes = totalChars.toFloat() / charsPerMinute
        return (minutes * 60).toInt().coerceAtLeast(30) // 최소 30초
    }
}