package com.mobi.tvseries.data

import com.google.gson.annotations.SerializedName

data class TvSeriesDetails(
    val id: Int,
    val name: String,
    val backdrop_path: String?,
    val seasons: List<Season>,
    val episodes: Int,
    val vote_average: Double,
    val overview: String
)
data class Season(
    @SerializedName("air_date")
    val airDate: String,

    @SerializedName("episode_count")
    val episodeCount: Int,

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("season_number")
    val seasonNumber: Int,

    @SerializedName("vote_average")
    val voteAverage: Double
)

