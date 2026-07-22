package com.fr1.companion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fr1.companion.domain.model.GuidanceCategory
import com.fr1.companion.domain.model.Severity
import com.fr1.companion.ui.chatbot.ChatScreen
import com.fr1.companion.ui.emergency.EmergencyAlertScreen
import com.fr1.companion.ui.guidance.GuidanceDetailScreen
import com.fr1.companion.ui.guidance.GuidanceListScreen
import com.fr1.companion.ui.history.HistoryScreen
import com.fr1.companion.ui.home.HomeScreen
import com.fr1.companion.ui.onboarding.LanguageSelectScreen
import com.fr1.companion.ui.settings.SettingsScreen
import com.fr1.companion.ui.splash.SplashScreen
import com.fr1.companion.ui.woundassessment.SeverityResultScreen
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
            WoundAssessmentScreen(
                onBack = { navController.popBackStack() },
                onComplete = { severity, category ->
                    navController.navigate(Routes.severityResult(severity.name, category?.name)) {
                        popUpTo(Routes.WOUND_ASSESSMENT) { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = Routes.SEVERITY_RESULT,
            arguments = listOf(
                navArgument(Routes.SEVERITY_RESULT_ARG) { type = NavType.StringType },
                navArgument(Routes.SEVERITY_RESULT_CATEGORY_ARG) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val severity = Severity.valueOf(
                backStackEntry.arguments?.getString(Routes.SEVERITY_RESULT_ARG) ?: Severity.MINOR.name,
            )
            val categoryArg = backStackEntry.arguments?.getString(Routes.SEVERITY_RESULT_CATEGORY_ARG)
            val recommendedCategory = categoryArg
                ?.takeIf { it != Routes.SEVERITY_RESULT_NO_CATEGORY }
                ?.let { GuidanceCategory.valueOf(it) }
            SeverityResultScreen(
                severity = severity,
                onViewGuidance = {
                    val route = recommendedCategory
                        ?.let { Routes.guidanceDetail(it.name) }
                        ?: Routes.GUIDANCE
                    navController.navigate(route) {
                        popUpTo(Routes.HOME)
                    }
                },
                onGoToEmergency = {
                    navController.navigate(Routes.EMERGENCY) {
                        popUpTo(Routes.HOME)
                    }
                },
                onBackToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.CHATBOT) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.GUIDANCE) {
            GuidanceListScreen(
                onBack = { navController.popBackStack() },
                onSelectCategory = { category ->
                    navController.navigate(Routes.guidanceDetail(category.name))
                },
            )
        }
        composable(
            route = Routes.GUIDANCE_DETAIL,
            arguments = listOf(navArgument(Routes.GUIDANCE_DETAIL_ARG) { type = NavType.StringType }),
        ) { backStackEntry ->
            val category = GuidanceCategory.valueOf(
                backStackEntry.arguments?.getString(Routes.GUIDANCE_DETAIL_ARG) ?: GuidanceCategory.CPR.name,
            )
            GuidanceDetailScreen(category = category, onBack = { navController.popBackStack() })
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
