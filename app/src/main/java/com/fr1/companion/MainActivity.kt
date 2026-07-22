package com.fr1.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fr1.companion.data.local.datastore.UserPreferencesRepository
import com.fr1.companion.ui.FR1App

class MainActivity : ComponentActivity() {

    private val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FR1App(userPreferencesRepository = userPreferencesRepository)
        }
    }
}
