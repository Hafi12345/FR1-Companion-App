package com.fr1.companion.ui.woundassessment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fr1.companion.data.local.db.WoundCaseRepository
import com.fr1.companion.domain.model.BleedingLevel
import com.fr1.companion.domain.model.GuidanceCategory
import com.fr1.companion.domain.model.Severity
import com.fr1.companion.domain.model.WoundAssessmentAnswers
import com.fr1.companion.domain.rules.WoundSeverityRuleEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val WOUND_ASSESSMENT_QUESTION_COUNT = 5

data class WoundAssessmentUiState(
    val currentStep: Int = 0,
    val isConscious: Boolean? = null,
    val breathingDifficulty: Boolean? = null,
    val bleeding: BleedingLevel? = null,
    val boneOrMuscleVisible: Boolean? = null,
    val woundLargerThanPalm: Boolean? = null,
    val photoUri: String? = null,
    val severity: Severity? = null,
    val recommendedCategory: GuidanceCategory? = null,
)

class WoundAssessmentViewModel(application: Application) : AndroidViewModel(application) {

    private val woundCaseRepository = WoundCaseRepository(application)

    private val _uiState = MutableStateFlow(WoundAssessmentUiState())
    val uiState: StateFlow<WoundAssessmentUiState> = _uiState.asStateFlow()

    private var hasPersistedCase = false

    fun answerConsciousness(value: Boolean) = advance { it.copy(isConscious = value) }
    fun answerBreathing(value: Boolean) = advance { it.copy(breathingDifficulty = value) }
    fun answerBleeding(value: BleedingLevel) = advance { it.copy(bleeding = value) }
    fun answerBoneVisible(value: Boolean) = advance { it.copy(boneOrMuscleVisible = value) }
    fun answerWoundSize(value: Boolean) = advance { it.copy(woundLargerThanPalm = value) }

    fun setPhotoUri(uri: String?) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    private inline fun advance(crossinline update: (WoundAssessmentUiState) -> WoundAssessmentUiState) {
        _uiState.update { current ->
            val next = update(current).copy(currentStep = current.currentStep + 1)
            if (next.currentStep < WOUND_ASSESSMENT_QUESTION_COUNT) next else finish(next)
        }
        persistIfComplete()
    }

    private fun finish(state: WoundAssessmentUiState): WoundAssessmentUiState {
        val answers = state.toAnswers()
        return state.copy(
            severity = WoundSeverityRuleEngine.assess(answers),
            recommendedCategory = WoundSeverityRuleEngine.recommendedGuidanceCategory(answers),
        )
    }

    // Runs after _uiState.update rather than inside its lambda: StateFlow.update
    // retries its transform under contention, so a suspend DB write can't live
    // there without risking a duplicate insert.
    private fun persistIfComplete() {
        val state = _uiState.value
        val severity = state.severity
        if (severity != null && !hasPersistedCase) {
            hasPersistedCase = true
            viewModelScope.launch {
                woundCaseRepository.save(state.toAnswers(), severity, state.photoUri)
            }
        }
    }

    private fun WoundAssessmentUiState.toAnswers() = WoundAssessmentAnswers(
        isConscious = isConscious ?: true,
        breathingDifficulty = breathingDifficulty ?: false,
        bleeding = bleeding ?: BleedingLevel.NONE,
        boneOrMuscleVisible = boneOrMuscleVisible ?: false,
        woundLargerThanPalm = woundLargerThanPalm ?: false,
    )
}
