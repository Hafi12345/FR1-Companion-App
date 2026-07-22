package com.fr1.companion.domain.model

enum class BleedingLevel { NONE, LIGHT, HEAVY }

data class WoundAssessmentAnswers(
    val isConscious: Boolean,
    val breathingDifficulty: Boolean,
    val bleeding: BleedingLevel,
    val boneOrMuscleVisible: Boolean,
    val woundLargerThanPalm: Boolean,
)
