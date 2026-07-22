package com.fr1.companion.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fr1.companion.R
import com.fr1.companion.ui.components.PlaceholderScreen

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    PlaceholderScreen(title = stringResource(R.string.feature_history), onBack = onBack)
}
