package com.fr1.companion.domain.rules

import com.fr1.companion.domain.model.BleedingLevel
import com.fr1.companion.domain.model.GuidanceCategory
import com.fr1.companion.domain.model.Severity
import com.fr1.companion.domain.model.WoundAssessmentAnswers

// SIMULATED: fixed decision tree for demo purposes only — not a clinically
// validated model. Any single red-flag answer (unconscious, breathing
// difficulty, heavy bleeding, visible bone/muscle) escalates straight to
// Severe. Absent those, meaningful bleeding or wound size escalates to
// Moderate. Everything else is Minor.
object WoundSeverityRuleEngine {

    fun assess(answers: WoundAssessmentAnswers): Severity {
        if (!answers.isConscious) return Severity.SEVERE
        if (answers.breathingDifficulty) return Severity.SEVERE
        if (answers.bleeding == BleedingLevel.HEAVY) return Severity.SEVERE
        if (answers.boneOrMuscleVisible) return Severity.SEVERE

        if (answers.bleeding == BleedingLevel.LIGHT || answers.woundLargerThanPalm) {
            return Severity.MODERATE
        }

        return Severity.MINOR
    }

    // SIMULATED: maps the same answers to the single most relevant guidance
    // category, in priority order, so a Severe/Moderate result can deep-link
    // straight to the matching page instead of dropping the user on the
    // general guidance list. Minor results with no red flags have no single
    // matching category — null means "show the general guidance list."
    fun recommendedGuidanceCategory(answers: WoundAssessmentAnswers): GuidanceCategory? {
        if (!answers.isConscious) return GuidanceCategory.POSITIONING
        if (answers.breathingDifficulty) return GuidanceCategory.CPR
        if (answers.bleeding == BleedingLevel.HEAVY) return GuidanceCategory.SEVERE_BLEEDING
        if (answers.boneOrMuscleVisible) return GuidanceCategory.FRACTURE
        if (answers.bleeding == BleedingLevel.LIGHT || answers.woundLargerThanPalm) {
            return GuidanceCategory.SEVERE_BLEEDING
        }
        return null
    }
}
