package com.fr1.companion.ui.emergency

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fr1.companion.R
import com.fr1.companion.ui.components.PlaceholderScreen

@Composable
fun EmergencyAlertScreen(onBack: () -> Unit) {
    PlaceholderScreen(title = stringResource(R.string.feature_emergency), onBack = onBack)
}
