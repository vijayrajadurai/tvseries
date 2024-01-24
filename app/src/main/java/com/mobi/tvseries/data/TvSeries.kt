package com.mobi.tvseries.data

data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdropPath: String?,
    val firstAirDate: String,
    val popularity: Double,
    val vote_average: Double,
    val voteCount: Int
)
