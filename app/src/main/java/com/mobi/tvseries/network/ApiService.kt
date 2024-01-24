package com.mobi.tvseries.network

import com.mobi.tvseries.data.SeriesResponse
import com.mobi.tvseries.data.TvSeriesDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("tv/popular?language=en-US")
    suspend fun getPopularSeries(@Query("page") page: Int?,
                                 @Query("api_key") apiKey: String
    ): Response<SeriesResponse>

    @GET("tv/{series_id}?language=en-US")
    suspend fun getTvSeriesDetails(
        @Path("series_id") tvId: Int?,
        @Query("api_key") apiKey: String
    ): Response<TvSeriesDetails>
}