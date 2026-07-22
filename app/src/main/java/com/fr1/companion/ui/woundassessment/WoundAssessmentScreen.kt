package com.fr1.companion.ui.woundassessment

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fr1.companion.R
import com.fr1.companion.domain.model.BleedingLevel
import com.fr1.companion.domain.model.GuidanceCategory
import com.fr1.companion.domain.model.Severity

@Composable
fun WoundAssessmentScreen(
    onBack: () -> Unit,
    onComplete: (Severity, GuidanceCategory?) -> Unit,
    viewModel: WoundAssessmentViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCamera by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.severity) {
        uiState.severity?.let { onComplete(it, uiState.recommendedCategory) }
    }

    if (showCamera) {
        CameraCaptureScreen(
            onCaptured = { uri: Uri ->
                viewModel.setPhotoUri(uri.toString())
                showCamera = false
            },
            onCancel = { showCamera = false },
        )
        return
    }

    WoundAssessmentContent(
        uiState = uiState,
        onBack = onBack,
        onAttachPhoto = { showCamera = true },
        onAnswerConsciousness = viewModel::answerConsciousness,
        onAnswerBreathing = viewModel::answerBreathing,
        onAnswerBleeding = viewModel::answerBleeding,
        onAnswerBoneVisible = viewModel::answerBoneVisible,
        onAnswerWoundSize = viewModel::answerWoundSize,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WoundAssessmentContent(
    uiState: WoundAssessmentUiState,
    onBack: () -> Unit,
    onAttachPhoto: () -> Unit,
    onAnswerConsciousness: (Boolean) -> Unit,
    onAnswerBreathing: (Boolean) -> Unit,
    onAnswerBleeding: (BleedingLevel) -> Unit,
    onAnswerBoneVisible: (Boolean) -> Unit,
    onAnswerWoundSize: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feature_wound_assessment),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Text(
                text = stringResource(R.string.wound_assessment_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(24.dp),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
        ) {
            PhotoAttachRow(photoAttached = uiState.photoUri != null, onAttachPhoto = onAttachPhoto)

            Text(
                text = stringResource(
                    R.string.wound_assessment_progress,
                    (uiState.currentStep + 1).coerceAtMost(WOUND_ASSESSMENT_QUESTION_COUNT),
                    WOUND_ASSESSMENT_QUESTION_COUNT,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )

            when (uiState.currentStep) {
                0 -> YesNoQuestion(stringResource(R.string.question_consciousness), onAnswerConsciousness)
                1 -> YesNoQuestion(stringResource(R.string.question_breathing), onAnswerBreathing)
                2 -> BleedingQuestion(onAnswerBleeding)
                3 -> YesNoQuestion(stringResource(R.string.question_bone_visible), onAnswerBoneVisible)
                4 -> YesNoQuestion(stringResource(R.string.question_wound_size), onAnswerWoundSize)
            }
        }
    }
}

@Composable
private fun PhotoAttachRow(photoAttached: Boolean, onAttachPhoto: () -> Unit) {
    OutlinedButton(onClick = onAttachPhoto, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(
                if (photoAttached) R.string.retake_photo else R.string.attach_photo,
            ),
        )
    }
}

@Composable
private fun QuestionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Composable
private fun YesNoQuestion(question: String, onAnswer: (Boolean) -> Unit) {
    Column {
        QuestionHeader(question)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { onAnswer(true) }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(stringResource(R.string.answer_yes), style = MaterialTheme.typography.labelLarge)
            }
            Button(onClick = { onAnswer(false) }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(stringResource(R.string.answer_no), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun BleedingQuestion(onAnswer: (BleedingLevel) -> Unit) {
    Column {
        QuestionHeader(stringResource(R.string.question_bleeding))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { onAnswer(BleedingLevel.NONE) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.bleeding_none), style = MaterialTheme.typography.labelLarge)
            }
            Button(onClick = { onAnswer(BleedingLevel.LIGHT) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.bleeding_light), style = MaterialTheme.typography.labelLarge)
            }
            Button(onClick = { onAnswer(BleedingLevel.HEAVY) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.bleeding_heavy), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
