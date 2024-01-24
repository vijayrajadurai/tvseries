package com.mobi.tvseries

import android.app.Application
import androidx.room.Room
import com.mobi.tvseries.data.AppDatabase
import com.mobi.tvseries.repository.SeriesRepository
import dagger.hilt.android.HiltAndroidApp

class MainApplication : Application() {
    // Declare lateinit var for the Room Database
//    lateinit var appDatabase: AppDatabase
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Initialize the Room Database in the onCreate method
//        appDatabase = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "tv_series_db"
//        ).build()
//    }

}