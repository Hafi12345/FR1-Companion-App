package com.fr1.companion.ui.chatbot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.R
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.data.network.OllamaMessage
import com.fr1.companion.data.network.OllamaRepository
import com.fr1.companion.domain.fallback.FallbackQAEngine
import com.fr1.companion.domain.model.ChatMessage
import com.fr1.companion.domain.model.ChatMode
import com.fr1.companion.domain.model.ChatSender
import com.fr1.companion.util.LocaleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = UserPreferencesRepository(application)
    private val ollamaRepository = OllamaRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _mode = MutableStateFlow(ChatMode.FALLBACK)
    val mode: StateFlow<ChatMode> = _mode.asStateFlow()

    private val _isCheckingConnection = MutableStateFlow(true)
    val isCheckingConnection: StateFlow<Boolean> = _isCheckingConnection.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private var liveModel: String? = null

    init {
        viewModelScope.launch {
            val languageCode = currentLanguage()
            _messages.value = listOf(botMessage(localizedString(languageCode, R.string.chat_greeting), ChatMode.FALLBACK))
        }
        checkConnection()
    }

    fun checkConnection() {
        viewModelScope.launch {
            _isCheckingConnection.value = true
            val url = preferencesRepository.ollamaServerUrlFlow.first()
            val model = ollamaRepository.checkReachableModel(url)
            liveModel = model
            _mode.value = if (model != null) ChatMode.LIVE else ChatMode.FALLBACK
            _isCheckingConnection.value = false
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || _isSending.value) return

        viewModelScope.launch {
            val languageCode = currentLanguage()
            _messages.update { it + ChatMessage(sender = ChatSender.USER, text = trimmed) }
            _isSending.value = true

            val currentModel = liveModel
            val reply = if (_mode.value == ChatMode.LIVE && currentModel != null) {
                val liveReply = ollamaRepository.sendMessage(
                    baseUrl = preferencesRepository.ollamaServerUrlFlow.first(),
                    model = currentModel,
                    history = buildOllamaHistory(),
                )
                if (liveReply != null) {
                    botMessage(liveReply, ChatMode.LIVE)
                } else {
                    // Live call failed mid-conversation — degrade to fallback for
                    // this message and reflect the change in the mode badge.
                    _mode.value = ChatMode.FALLBACK
                    fallbackReply(languageCode, trimmed)
                }
            } else {
                fallbackReply(languageCode, trimmed)
            }

            _messages.update { it + reply }
            _isSending.value = false
        }
    }

    private fun fallbackReply(languageCode: String, userMessage: String): ChatMessage {
        val answerRes = FallbackQAEngine.answerRes(userMessage)
        return botMessage(localizedString(languageCode, answerRes), ChatMode.FALLBACK)
    }

    private fun buildOllamaHistory(): List<OllamaMessage> {
        val systemPrompt = OllamaMessage(role = "system", content = SAFETY_SYSTEM_PROMPT)
        val conversation = _messages.value.map { message ->
            OllamaMessage(
                role = if (message.sender == ChatSender.USER) "user" else "assistant",
                content = message.text,
            )
        }
        return listOf(systemPrompt) + conversation
    }

    private suspend fun currentLanguage(): String =
        preferencesRepository.languageFlow.first() ?: UserPreferencesRepository.LANGUAGE_ENGLISH

    private fun localizedString(languageCode: String, resId: Int): String =
        LocaleManager.localizedContext(getApplication(), languageCode).getString(resId)

    private fun botMessage(text: String, mode: ChatMode) =
        ChatMessage(sender = ChatSender.BOT, text = text, mode = mode)

    companion object {
        private const val SAFETY_SYSTEM_PROMPT = "You are a calm, concise bystander first-aid assistant for a " +
            "mobile emergency-response app. Only give basic first-aid stabilization guidance (bleeding control, " +
            "positioning, safety, CPR overview, calling for help). Never mention medication names, dosages, or " +
            "injections. Never give surgical or invasive instructions. Never claim diagnostic certainty. For " +
            "anything serious or unclear, tell the user to call emergency services immediately. Keep responses " +
            "short (2-4 sentences) and reassuring. Respond in the same language the user is writing in."
    }
}
