package com.fr1.companion.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WoundCaseDao {

    @Insert
    suspend fun insert(case: WoundCaseEntity): Long

    @Query("SELECT * FROM wound_cases ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<WoundCaseEntity>>
}
