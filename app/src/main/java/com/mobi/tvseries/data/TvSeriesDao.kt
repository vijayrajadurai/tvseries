package com.mobi.tvseries.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TvSeriesDao {
    @Query("SELECT * FROM tv_series_table")
    suspend fun getAllTvSeries(): List<TvSeriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvSeries(tvSeries: List<TvSeriesEntity>)
}