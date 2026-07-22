package com.fr1.companion.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fr1.companion.R
import com.fr1.companion.ui.components.NavCard
import com.fr1.companion.ui.navigation.Routes
import com.fr1.companion.ui.theme.FR1CompanionAppTheme

@Composable
fun HomeScreen(onNavigate: (route: String) -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NavCard(
                    title = stringResource(R.string.feature_wound_assessment),
                    onClick = { onNavigate(Routes.WOUND_ASSESSMENT) },
                )
                NavCard(
                    title = stringResource(R.string.feature_chatbot),
                    onClick = { onNavigate(Routes.CHATBOT) },
                )
                NavCard(
                    title = stringResource(R.string.feature_guidance),
                    onClick = { onNavigate(Routes.GUIDANCE) },
                )
                NavCard(
                    title = stringResource(R.string.feature_emergency),
                    onClick = { onNavigate(Routes.EMERGENCY) },
                )
                NavCard(
                    title = stringResource(R.string.feature_history),
                    onClick = { onNavigate(Routes.HISTORY) },
                )
                NavCard(
                    title = stringResource(R.string.feature_settings),
                    onClick = { onNavigate(Routes.SETTINGS) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    FR1CompanionAppTheme {
        HomeScreen(onNavigate = {})
    }
}
