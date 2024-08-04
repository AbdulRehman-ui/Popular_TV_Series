package com.project.populartvseries.apiViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.populartvseries.common.Resource
import com.project.populartvseries.repositories.SeriesRepository
import com.project.populartvseries.response.PopularSeriesResponse
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
}


