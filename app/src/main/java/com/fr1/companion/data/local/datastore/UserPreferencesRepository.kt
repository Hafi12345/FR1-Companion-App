package com.fr1.companion.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "fr1_settings")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_URDU = "ur"
        const val DEFAULT_OLLAMA_SERVER_URL = "http://192.168.1.100:11434"

        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val OLLAMA_SERVER_URL_KEY = stringPreferencesKey("ollama_server_url")
    }

    /** Null until the user picks a language on first launch. */
    val languageFlow: Flow<String?> = context.dataStore.data.map { it[LANGUAGE_KEY] }

    val ollamaServerUrlFlow: Flow<String> = context.dataStore.data.map {
        it[OLLAMA_SERVER_URL_KEY] ?: DEFAULT_OLLAMA_SERVER_URL
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = languageCode }
    }

    suspend fun setOllamaServerUrl(url: String) {
        context.dataStore.edit { it[OLLAMA_SERVER_URL_KEY] = url }
    }
}
