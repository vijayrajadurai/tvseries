package com.mobi.tvseries.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_series_table")
data class TvSeriesEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val firstAirDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int
)

