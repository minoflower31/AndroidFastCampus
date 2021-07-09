package com.minoflower.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import dao.HistoryDao
import model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}