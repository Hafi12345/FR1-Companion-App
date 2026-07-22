package com.fr1.companion.ui.guidance

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fr1.companion.R
import com.fr1.companion.ui.components.PlaceholderScreen

@Composable
fun GuidanceListScreen(onBack: () -> Unit) {
    PlaceholderScreen(title = stringResource(R.string.feature_guidance), onBack = onBack)
}
