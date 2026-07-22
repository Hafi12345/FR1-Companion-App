package com.fr1.companion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fr1.companion.ui.chatbot.ChatScreen
import com.fr1.companion.ui.emergency.EmergencyAlertScreen
import com.fr1.companion.ui.guidance.GuidanceListScreen
import com.fr1.companion.ui.history.HistoryScreen
import com.fr1.companion.ui.home.HomeScreen
import com.fr1.companion.ui.onboarding.LanguageSelectScreen
import com.fr1.companion.ui.settings.SettingsScreen
import com.fr1.companion.ui.splash.SplashScreen
import com.fr1.companion.ui.woundassessment.WoundAssessmentScreen

@Composable
fun FR1NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.LANGUAGE_SELECT) {
            LanguageSelectScreen(
                onLanguageSelected = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LANGUAGE_SELECT) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.HOME) {
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable(Routes.WOUND_ASSESSMENT) {
            WoundAssessmentScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CHATBOT) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.GUIDANCE) {
            GuidanceListScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.EMERGENCY) {
            EmergencyAlertScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.HISTORY) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
