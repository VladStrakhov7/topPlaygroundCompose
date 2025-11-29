package com.example.topplaygroundcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.topplaygroundcompose.data.local.entity.MatchEntity

@Database(entities = [MatchEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    
    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "matches_database"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}

