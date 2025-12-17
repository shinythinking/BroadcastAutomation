package com.shinythinking.broadcastautomation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shinythinking.broadcastautomation.presentation.ai.AIGenerationScreen
import com.shinythinking.broadcastautomation.presentation.archive.ArchiveScreen
import com.shinythinking.broadcastautomation.presentation.broadcast.BroadcastScreen
import com.shinythinking.broadcastautomation.presentation.home.HomeScreen
import com.shinythinking.broadcastautomation.presentation.script_edit.ScriptEditScreen
import com.shinythinking.broadcastautomation.presentation.template_edit.TemplateEditScreen
import com.shinythinking.broadcastautomation.presentation.template_list.TemplateListScreen
import com.shinythinking.broadcastautomation.presentation.tts.TTSSettingsScreen
import com.shinythinking.broadcastautomation.presentation.voice_preview.VoicePreviewScreen

@Composable
fun VillageBroadcastNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main
    ) {
        composable<Screen.Main> {
            HomeScreen(
                onNavigateToTemplate = { navController.navigate(Screen.TemplateList) },
                onNavigateToAI = { navController.navigate(Screen.AIGeneration) },
                onNavigateToArchive = { navController.navigate(Screen.Archive) }
            )
        }

        composable<Screen.TemplateList> {
            TemplateListScreen(
                onBackClick = { navController.navigateUp() },
                onTemplateSelect = { templateId ->
                    navController.navigate(Screen.TemplateEdit(templateId))
                }
            )
        }

        composable<Screen.TemplateEdit> { backStackEntry ->
            TemplateEditScreen(
                onBackClick = { navController.navigateUp() },
                onScriptGenerated = { scriptId ->
                    navController.navigate(Screen.ScriptEdit(scriptId)) {
                        popUpTo(Screen.Main)
                    }
                }
            )
        }

        composable<Screen.AIGeneration> {
            AIGenerationScreen(
                onBackClick = { navController.navigateUp() },
                onScriptGenerated = { scriptId ->
                    navController.navigate(Screen.ScriptEdit(scriptId)) {
                        popUpTo(Screen.Main)
                    }
                }
            )
        }

        composable<Screen.Archive> {
            ArchiveScreen(
                onBackClick = { navController.navigateUp() },
                onScriptSelect = { scriptId ->
                    navController.navigate(Screen.ScriptEdit(scriptId))
                }
            )
        }

        composable<Screen.ScriptEdit> { backStackEntry ->
            backStackEntry.toRoute<Screen.ScriptEdit>()
            ScriptEditScreen(
                onBackClick = { navController.navigateUp() },
                onConvertToVoice = { scriptId ->
                    navController.navigate(Screen.TTSSettings(scriptId))
                }
            )
        }

        composable<Screen.TTSSettings> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TTSSettings>()
            TTSSettingsScreen(
                scriptId = args.scriptId,
                onBackClick = { navController.navigateUp() },
                onVoiceGenerated = { scriptId ->
                    navController.navigate(Screen.VoicePreview(scriptId))
                }
            )
        }

        composable<Screen.VoicePreview> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.VoicePreview>()
            VoicePreviewScreen(
                scriptId = args.scriptId,
                onBackClick = { navController.navigateUp() },
                onBroadcast = { scriptId ->
                    navController.navigate(Screen.Broadcast(scriptId))
                }
            )
        }

        composable<Screen.Broadcast> { backStackEntry ->
            backStackEntry.toRoute<Screen.Broadcast>()
            BroadcastScreen(
                onBackClick = { navController.navigateUp() },
                onNavigateToMain = {
                    navController.navigate(Screen.Main) {
                        popUpTo(Screen.Main) { inclusive = true }
                    }
                }
            )
        }
    }
}
