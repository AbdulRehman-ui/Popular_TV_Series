package com.project.populartvseries.api

import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getPopularSeries(language : String, apiKey : String) : Response<PopularSeriesResponse>

    suspend fun getSeriesDetails(seriesId : String, language: String, apiKey: String) : Response<SeriesDetailsResponse>

}