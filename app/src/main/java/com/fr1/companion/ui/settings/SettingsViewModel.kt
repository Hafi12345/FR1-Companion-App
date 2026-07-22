package com.fr1.companion.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.data.network.OllamaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ConnectionTestState {
    data object Idle : ConnectionTestState
    data object Testing : ConnectionTestState
    data class Success(val modelName: String) : ConnectionTestState
    data object Failure : ConnectionTestState
}

private val OLLAMA_URL_REGEX = Regex("^https?://[^\\s:/]+(:\\d+)?/?$")

fun isValidOllamaUrl(url: String): Boolean = OLLAMA_URL_REGEX.matches(url.trim())

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)
    private val ollamaRepository = OllamaRepository()

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

    private val _connectionTestState = MutableStateFlow<ConnectionTestState>(ConnectionTestState.Idle)
    val connectionTestState: StateFlow<ConnectionTestState> = _connectionTestState.asStateFlow()

    fun setLanguage(languageCode: String) {
        viewModelScope.launch { repository.setLanguage(languageCode) }
    }

    fun setOllamaServerUrl(url: String) {
        _connectionTestState.value = ConnectionTestState.Idle
        viewModelScope.launch { repository.setOllamaServerUrl(url) }
    }

    fun testConnection() {
        val url = ollamaServerUrl.value
        if (!isValidOllamaUrl(url)) return
        viewModelScope.launch {
            _connectionTestState.value = ConnectionTestState.Testing
            val model = ollamaRepository.checkReachableModel(url)
            _connectionTestState.value = if (model != null) {
                ConnectionTestState.Success(model)
            } else {
                ConnectionTestState.Failure
            }
        }
    }
}
