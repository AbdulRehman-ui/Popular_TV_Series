package com.project.populartvseries.apiViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.populartvseries.common.Resource
import com.project.populartvseries.repositories.SeriesRepository
import com.project.populartvseries.response.ResultsItemPopular
import kotlinx.coroutines.launch

class SeriesViewModel(private val repository: SeriesRepository) : ViewModel() {

    private val _tvSeries = MutableLiveData<Resource<List<ResultsItemPopular>>>()
    val tvSeries: LiveData<Resource<List<ResultsItemPopular>>> get() = _tvSeries

    fun getPopularTVSeries(language: String) {
        viewModelScope.launch {
            _tvSeries.value = Resource.loading(null)
            try {
                repository.getPopularTVSeries(language).observeForever {
                    _tvSeries.value = it
                }
            } catch (e: Exception) {
                _tvSeries.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

}


