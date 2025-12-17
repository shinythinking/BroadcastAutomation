package com.shinythinking.broadcastautomation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main : Screen

    @Serializable
    data object TemplateList : Screen

    @Serializable
    data class TemplateEdit(val templateId: String) : Screen

    @Serializable
    data object AIGeneration : Screen

    @Serializable
    data object Archive : Screen

    @Serializable
    data class ScriptEdit(val scriptId: String) : Screen

    @Serializable
    data class TTSSettings(val scriptId: String) : Screen

    @Serializable
    data class VoicePreview(val scriptId: String) : Screen

    @Serializable
    data class Broadcast(val scriptId: String) : Screen
}