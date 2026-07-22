package com.fr1.companion.data.local.db

import android.content.Context
import com.fr1.companion.domain.model.Severity
import com.fr1.companion.domain.model.WoundAssessmentAnswers
import kotlinx.coroutines.flow.Flow

class WoundCaseRepository(context: Context) {

    private val dao = FR1Database.getInstance(context).woundCaseDao()

    fun observeAll(): Flow<List<WoundCaseEntity>> = dao.observeAll()

    suspend fun save(answers: WoundAssessmentAnswers, severity: Severity, photoUri: String?) {
        dao.insert(
            WoundCaseEntity(
                timestamp = System.currentTimeMillis(),
                severity = severity.name,
                isConscious = answers.isConscious,
                breathingDifficulty = answers.breathingDifficulty,
                bleeding = answers.bleeding.name,
                boneOrMuscleVisible = answers.boneOrMuscleVisible,
                woundLargerThanPalm = answers.woundLargerThanPalm,
                photoUri = photoUri,
            ),
        )
    }
}
