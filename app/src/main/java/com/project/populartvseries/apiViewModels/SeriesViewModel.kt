package com.project.populartvseries.apiViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.populartvseries.common.Resource
import com.project.populartvseries.repositories.SeriesRepository
import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SeriesViewModel @Inject constructor(
    val mainRepository: SeriesRepository
)  :  ViewModel() {

    private val _res_popular_series = MutableLiveData<Resource<PopularSeriesResponse>>()

    val res_popular_series: LiveData<Resource<PopularSeriesResponse>>
        get() = _res_popular_series

    fun getPopularSeries(language: String) = viewModelScope.launch {
        _res_popular_series.postValue(Resource.loading(null))
        mainRepository.getPopularSeries(language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_popular_series.postValue(Resource.success(it.body()))
            } else {
                _res_popular_series.postValue(Resource.error(it.message(), null))
            }
        }
    }

    private val _res_series_details = MutableLiveData<Resource<SeriesDetailsResponse>>()

    val res_series_details: LiveData<Resource<SeriesDetailsResponse>>
        get() = _res_series_details

    fun getSeriesDetails(seriesId : String, language : String) = viewModelScope.launch {
        _res_series_details.postValue(Resource.loading(null))
        mainRepository.getSeriesDetails(seriesId, language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_series_details.postValue(Resource.success(it.body()))
            } else {
                _res_series_details.postValue(Resource.error(it.message(), null))
            }
        }
    }

    private val _res_season_details = MutableLiveData<Resource<SeasonDetailsResponse>>()

    val res_season_details: LiveData<Resource<SeasonDetailsResponse>>
        get() = _res_season_details

    fun getSeasonDetails(seriesId : String, seasonId : String, language : String) = viewModelScope.launch {
        _res_season_details.postValue(Resource.loading(null))
        mainRepository.getSeasonDetails(seriesId, seasonId, language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_season_details.postValue(Resource.success(it.body()))
            } else {
                _res_season_details.postValue(Resource.error(it.message(), null))
            }
        }
    }


    private val _res_search_details = MutableLiveData<Resource<SearchSeriesResponse>>()

    val res_search_details: LiveData<Resource<SearchSeriesResponse>>
        get() = _res_search_details

    fun getsearchDetails(query : String, language : String) = viewModelScope.launch {
        _res_search_details.postValue(Resource.loading(null))
        mainRepository.getSearchSeriesDetails(query, language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_search_details.postValue(Resource.success(it.body()))
            } else {
                _res_search_details.postValue(Resource.error(it.message(), null))
            }
        }
    }

}


