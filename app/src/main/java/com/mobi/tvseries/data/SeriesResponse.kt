package com.mobi.tvseries.data

data class SeriesResponse(
    val page: Int,
    val results: List<TvSeries>,
    val totalPages: Int,
    val totalResults: Int
)
