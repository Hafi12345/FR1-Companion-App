package com.fr1.companion.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.ui.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MIN_SPLASH_DISPLAY_MS = 500L

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val language = repository.languageFlow.first()
            delay(MIN_SPLASH_DISPLAY_MS)
            _destination.value = if (language == null) Routes.LANGUAGE_SELECT else Routes.HOME
        }
    }
}
