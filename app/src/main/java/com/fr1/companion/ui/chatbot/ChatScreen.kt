package com.fr1.companion.ui.chatbot

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fr1.companion.R
import com.fr1.companion.ui.components.PlaceholderScreen

@Composable
fun ChatScreen(onBack: () -> Unit) {
    PlaceholderScreen(title = stringResource(R.string.feature_chatbot), onBack = onBack)
}
