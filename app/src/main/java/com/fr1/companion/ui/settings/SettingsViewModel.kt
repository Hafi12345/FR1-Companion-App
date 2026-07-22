package com.fr1.companion.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    val language: StateFlow<String> = repository.languageFlow
        .map { it ?: UserPreferencesRepository.LANGUAGE_ENGLISH }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UserPreferencesRepository.LANGUAGE_ENGLISH,
        )

    val ollamaServerUrl: StateFlow<String> = repository.ollamaServerUrlFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UserPreferencesRepository.DEFAULT_OLLAMA_SERVER_URL,
    )

    fun setLanguage(languageCode: String) {
        viewModelScope.launch { repository.setLanguage(languageCode) }
    }

    fun setOllamaServerUrl(url: String) {
        viewModelScope.launch { repository.setOllamaServerUrl(url) }
    }
}
