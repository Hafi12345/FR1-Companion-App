package com.fr1.companion.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WoundCaseEntity::class], version = 1, exportSchema = false)
abstract class FR1Database : RoomDatabase() {

    abstract fun woundCaseDao(): WoundCaseDao

    companion object {
        @Volatile
        private var instance: FR1Database? = null

        fun getInstance(context: Context): FR1Database =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FR1Database::class.java,
                    "fr1_database",
                ).build().also { instance = it }
            }
    }
}
