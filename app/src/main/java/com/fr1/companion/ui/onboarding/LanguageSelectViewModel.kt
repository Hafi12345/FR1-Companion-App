package com.fr1.companion.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import kotlinx.coroutines.launch

class LanguageSelectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    fun selectLanguage(languageCode: String, onSelected: () -> Unit) {
        viewModelScope.launch {
            repository.setLanguage(languageCode)
            onSelected()
        }
    }
}
