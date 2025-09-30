package com.project.populartvseries.repositories

import androidx.lifecycle.LiveData
import com.project.populartvseries.api.ApiHelper
import com.project.populartvseries.room.dao.PopularSeriesDao
import com.project.populartvseries.room.entities.PopularSeriesEntity
import javax.inject.Inject

/*
     * Repository is a class that is mainly used to manage multiple sources of data.
     * The repository class isolates the data sources from the rest of the apps and provides a clean api for data access
       to the rest of the app
     * The repository can gather data from different data sources(different REST APIs, cache, local database storage) and
       it provides this data to the rest of the app
 */

/*
        The SeriesRepository class follows the Repository Pattern, which abstracts the data sources
        (e.g., network and local database) and provides a clean API for data access. This helps in
        managing data from multiple sources and improves the testability of the code.
 */

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