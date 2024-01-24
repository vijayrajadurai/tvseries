package com.mobi.tvseries.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TvSeriesEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tvSeriesDao():TvSeriesDao
}