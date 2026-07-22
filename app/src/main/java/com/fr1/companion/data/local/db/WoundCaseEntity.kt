package com.fr1.companion.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wound_cases")
data class WoundCaseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val severity: String,
    val isConscious: Boolean,
    val breathingDifficulty: Boolean,
    val bleeding: String,
    val boneOrMuscleVisible: Boolean,
    val woundLargerThanPalm: Boolean,
    val photoUri: String?,
)
