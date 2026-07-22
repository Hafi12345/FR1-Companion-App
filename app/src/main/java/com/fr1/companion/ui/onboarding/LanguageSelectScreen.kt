package com.fr1.companion.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fr1.companion.R
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.ui.theme.FR1CompanionAppTheme

@Composable
fun LanguageSelectScreen(
    onLanguageSelected: () -> Unit,
    viewModel: LanguageSelectViewModel = viewModel(),
) {
    LanguageSelectContent(
        onEnglishSelected = {
            viewModel.selectLanguage(UserPreferencesRepository.LANGUAGE_ENGLISH, onLanguageSelected)
        },
        onUrduSelected = {
            viewModel.selectLanguage(UserPreferencesRepository.LANGUAGE_URDU, onLanguageSelected)
        },
    )
}

@Composable
private fun LanguageSelectContent(
    onEnglishSelected: () -> Unit,
    onUrduSelected: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.language_select_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Column(
            modifier = Modifier.padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                onClick = onEnglishSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text(text = stringResource(R.string.language_name_english), style = MaterialTheme.typography.labelLarge)
            }
            Button(
                onClick = onUrduSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text(text = stringResource(R.string.language_name_urdu), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageSelectPreview() {
    FR1CompanionAppTheme {
        LanguageSelectContent(onEnglishSelected = {}, onUrduSelected = {})
    }
}
