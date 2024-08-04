package com.project.populartvseries.repositories

import com.project.populartvseries.api.ApiHelper
import javax.inject.Inject

class SeriesRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun getPopularSeries(language : String, apiKey : String) = apiHelper.getPopularSeries(language, apiKey)

    suspend fun getSeriesDetails(seriesId : String, language : String, apiKey : String) = apiHelper.getSeriesDetails(seriesId, language, apiKey)

}