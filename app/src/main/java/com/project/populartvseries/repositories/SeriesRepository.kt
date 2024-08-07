package com.project.populartvseries.repositories

import androidx.lifecycle.LiveData
import com.project.populartvseries.api.ApiHelper
import com.project.populartvseries.room.dao.PopularSeriesDao
import com.project.populartvseries.room.entities.PopularSeriesEntity
import javax.inject.Inject

class SeriesRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val popularSeriesDao: PopularSeriesDao
) {

    suspend fun getPopularSeries(language : String, page: Int, apiKey : String) = apiHelper.getPopularSeries(language, page, apiKey)

    suspend fun popularSeriesLocal(seriesEntity: List<PopularSeriesEntity>) = popularSeriesDao.insertPopularSeries(seriesEntity)

    fun getPopularSeriesFromLocal(): LiveData<List<PopularSeriesEntity>> = popularSeriesDao.getPopularSeries()

    suspend fun getSeriesDetails(seriesId : String, language : String, apiKey : String) = apiHelper.getSeriesDetails(seriesId, language, apiKey)

    suspend fun getSeasonDetails(seriesId : String, seasonId : String,  language : String, apiKey : String) = apiHelper.getSeasonDetails(seriesId, seasonId, language, apiKey)

    suspend fun getSearchSeriesDetails(query : String, language : String, page: Int, apiKey : String) = apiHelper.getSearchSeasonDetails(query, language, page, apiKey)

}