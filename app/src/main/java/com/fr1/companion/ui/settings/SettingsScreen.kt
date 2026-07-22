package com.fr1.companion.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fr1.companion.R
import com.fr1.companion.data.local.datastore.UserPreferencesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val ollamaServerUrl by viewModel.ollamaServerUrl.collectAsStateWithLifecycle()
    var ollamaUrlInput by remember(ollamaServerUrl) { mutableStateOf(ollamaServerUrl) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feature_settings),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_language_section),
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
            ) {
                val isEnglish = language == UserPreferencesRepository.LANGUAGE_ENGLISH
                val languageButton: @Composable (String, Boolean, () -> Unit) -> Unit = { label, selected, onClick ->
                    if (selected) {
                        Button(onClick = onClick, modifier = Modifier.padding(end = 12.dp)) {
                            Text(text = label, style = MaterialTheme.typography.labelLarge)
                        }
                    } else {
                        OutlinedButton(onClick = onClick, modifier = Modifier.padding(end = 12.dp)) {
                            Text(text = label, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                languageButton(stringResource(R.string.language_name_english), isEnglish) {
                    viewModel.setLanguage(UserPreferencesRepository.LANGUAGE_ENGLISH)
                }
                languageButton(stringResource(R.string.language_name_urdu), !isEnglish) {
                    viewModel.setLanguage(UserPreferencesRepository.LANGUAGE_URDU)
                }
            }

            Text(
                text = stringResource(R.string.settings_ollama_section),
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedTextField(
                value = ollamaUrlInput,
                onValueChange = {
                    ollamaUrlInput = it
                    viewModel.setOllamaServerUrl(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = { Text(stringResource(R.string.settings_ollama_url_label)) },
                singleLine = true,
            )
        }
    }
}
