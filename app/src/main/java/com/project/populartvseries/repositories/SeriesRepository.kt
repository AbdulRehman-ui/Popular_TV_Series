package com.project.populartvseries.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.project.populartvseries.api.ApiService
import com.project.populartvseries.common.Resource
import com.project.populartvseries.response.ResultsItemPopular
import kotlinx.coroutines.Dispatchers

class SeriesRepository(private val apiService: ApiService, private val apikey : String) {

    fun getPopularTVSeries(language: String): LiveData<Resource<List<ResultsItemPopular>>> = liveData(
        Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiService.getPopularSeries(language, apikey)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.success(it.results!!.filterNotNull()))
                } ?: emit(Resource.error("No results found", null))
            } else {
                emit(Resource.error(response.message(), null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "An error occurred", null))
        }
    }

}