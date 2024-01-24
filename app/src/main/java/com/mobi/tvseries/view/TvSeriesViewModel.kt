package com.mobi.tvseries.view

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mobi.tvseries.MainApplication
import com.mobi.tvseries.data.TvSeries
import com.mobi.tvseries.data.TvSeriesDetails
import com.mobi.tvseries.data.TvSeriesEntity
import com.mobi.tvseries.network.ApiClient
import com.mobi.tvseries.repository.SeriesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TvSeriesViewModel: ViewModel() {

//    private val repository = SeriesRepository()

    val apiKey: String = "be79fab8cd4ecb5258272f2dd42092f4"


    private val _seriesList = MutableLiveData<List<TvSeries>?>(emptyList())
    val seriesList: MutableLiveData<List<TvSeries>?> get() = _seriesList

    private val _currentPage = MutableLiveData(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _seriesDetails = MutableLiveData<TvSeriesDetails>()
    val seriesDetails: LiveData<TvSeriesDetails> get() = _seriesDetails


    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.Success)
    val loadingState: LiveData<LoadingState> = _loadingState

//    private val appDatabase = (application as MainApplication).appDatabase

    init {
        fetchTVSeries()
    }

    fun fetchTVSeries() {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.Loading

                delay(3000)
                Log.e("CurrentPage", "${currentPage.value}")
                val response = ApiClient.apiService.getPopularSeries(
                    currentPage.value,
                    apiKey
                )
                val requestUrl = response.raw().request().url().toString()
                Log.e("Request", "Request URL: $requestUrl")
                if (response.isSuccessful) {
                    val tvSeriesList = response.body()?.results
//                    tvSeriesList?.let {
//                        appDatabase.tvSeriesDao().insertTvSeries(it.map { series ->
//                            TvSeriesEntity(
//                                series.id,
//                                series.name,
//                                series.overview,
//                                series.poster_path,
//                                series.backdropPath,
//                                series.firstAirDate,
//                                series.popularity,
//                                series.vote_average,
//                                series.voteCount
//                            )
//                        })
//                    }
                    _seriesList.value = tvSeriesList
                    _loadingState.value = LoadingState.Success
                    Log.e("ApiCall", "Response: ${response.body()}")
                } else {
                    _loadingState.value = LoadingState.Error("Failed to load details")
                    Log.e("ApiCall", "Error: ${response.code()}, ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Error("An error occurred: ${e.message}")
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun refreshSeries() {
        _currentPage.value = _currentPage.value?.plus(1)
        Log.e("CurrentPage", "${_currentPage.value}")
        fetchTVSeries()
    }

    fun filterSeries(query: String) {
        val filteredList = _seriesList.value?.filter { series ->
            series.name.contains(query, ignoreCase = true)
        }
        _seriesList.value = filteredList
    }

    fun fetchTvSeriesDetails(tvId: Int) {
        viewModelScope.launch {
            try {
                delay(3000)
                val response = ApiClient.apiService.getTvSeriesDetails(tvId, apiKey)
                val requestUrl = response.raw().request().url().toString()
                Log.e("Request", "Details Request URL: $requestUrl")
                if (response.isSuccessful) {
                    _seriesDetails.value = response.body()
                    _loadingState.value = LoadingState.Success
                    Log.e("ApiCall", "Details Response: ${response.body()}")
                } else {
                    _loadingState.value = LoadingState.Error("Failed to load details")
                    Log.e(
                        "ApiCall",
                        "Details Error: ${response.code()}, ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Error("An error occurred: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

sealed class LoadingState {
    object Loading : LoadingState()
    object Success : LoadingState()
    data class Error(val errorMessage: String) : LoadingState()
}
