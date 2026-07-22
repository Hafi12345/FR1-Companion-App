package com.fr1.companion.ui.woundassessment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fr1.companion.R
import com.fr1.companion.domain.model.Severity
import com.fr1.companion.ui.theme.FR1CompanionAppTheme
import com.fr1.companion.ui.theme.SeverityMinor
import com.fr1.companion.ui.theme.SeverityModerate
import com.fr1.companion.ui.theme.SeveritySevere

@Composable
fun SeverityResultScreen(
    severity: Severity,
    onViewGuidance: () -> Unit,
    onGoToEmergency: () -> Unit,
    onBackToHome: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.severity_result_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            SeverityBadge(severity)

            Text(
                text = stringResource(severity.descriptionRes()),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
            )

            when (severity) {
                Severity.SEVERE -> {
                    Button(
                        onClick = onGoToEmergency,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SeveritySevere),
                    ) {
                        Text(stringResource(R.string.action_go_to_emergency), style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = onViewGuidance,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    ) {
                        Text(stringResource(R.string.action_view_guidance), style = MaterialTheme.typography.labelLarge)
                    }
                }
                Severity.MODERATE, Severity.MINOR -> {
                    Button(onClick = onViewGuidance, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.action_view_guidance), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Text(stringResource(R.string.action_back_to_home), style = MaterialTheme.typography.labelLarge)
            }

            Text(
                text = stringResource(R.string.wound_assessment_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 24.dp),
            )
        }
    }
}

@Composable
private fun SeverityBadge(severity: Severity) {
    val (color, icon, labelRes) = when (severity) {
        Severity.MINOR -> Triple(SeverityMinor, Icons.Filled.CheckCircle, R.string.severity_minor_label)
        Severity.MODERATE -> Triple(SeverityModerate, Icons.Filled.Warning, R.string.severity_moderate_label)
        Severity.SEVERE -> Triple(SeveritySevere, Icons.Filled.Clear, R.string.severity_severe_label)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color, shape = RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
        )
    }
}

private fun Severity.descriptionRes(): Int = when (this) {
    Severity.MINOR -> R.string.severity_minor_description
    Severity.MODERATE -> R.string.severity_moderate_description
    Severity.SEVERE -> R.string.severity_severe_description
}

@Preview(showBackground = true)
@Composable
private fun SeverityResultSeverePreview() {
    FR1CompanionAppTheme {
        SeverityResultScreen(Severity.SEVERE, onViewGuidance = {}, onGoToEmergency = {}, onBackToHome = {})
    }
}
