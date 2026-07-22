package com.fr1.companion.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.ui.navigation.FR1NavGraph
import com.fr1.companion.ui.theme.FR1CompanionAppTheme
import com.fr1.companion.util.LocaleManager

/**
 * Root composable: applies the user's selected language app-wide (via a
 * locale-scoped Context so `stringResource()` resolves correctly) and the
 * matching layout direction, without needing an Activity recreation.
 */
@Composable
fun FR1App(userPreferencesRepository: UserPreferencesRepository) {
    val language by userPreferencesRepository.languageFlow.collectAsStateWithLifecycle(initialValue = null)
    val languageCode = language ?: UserPreferencesRepository.LANGUAGE_ENGLISH

    val baseContext = LocalContext.current
    val localizedContext = remember(languageCode) {
        LocaleManager.localizedContext(baseContext, languageCode)
    }
    val layoutDirection = if (LocaleManager.isRtl(languageCode)) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides layoutDirection,
    ) {
        FR1CompanionAppTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                FR1NavGraph()
            }
        }
    }
}
