package com.fr1.companion.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Reachability/chat calls build a fresh client per request since the Ollama
// server IP is user-configurable at runtime (see SettingsScreen) rather than
// fixed at app startup.
class OllamaRepository {

    private fun buildService(baseUrl: String): OllamaApiService {
        val normalizedUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        val client = OkHttpClient.Builder()
            .connectTimeout(REACHABILITY_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(CHAT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(REACHABILITY_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OllamaApiService::class.java)
    }

    // Returns the first available model name if Ollama is reachable and has at
    // least one model pulled, or null otherwise — either case means live chat
    // isn't usable and the caller should fall back.
    suspend fun checkReachableModel(baseUrl: String): String? = try {
        buildService(baseUrl).listModels().takeIf { it.isSuccessful }
            ?.body()?.models?.firstOrNull()?.name
    } catch (e: Exception) {
        null
    }

    suspend fun sendMessage(baseUrl: String, model: String, history: List<OllamaMessage>): String? = try {
        buildService(baseUrl).chat(OllamaChatRequest(model = model, messages = history)).message?.content
    } catch (e: Exception) {
        null
    }

    companion object {
        private const val REACHABILITY_TIMEOUT_MS = 2500L
        private const val CHAT_TIMEOUT_MS = 15000L
    }
}
