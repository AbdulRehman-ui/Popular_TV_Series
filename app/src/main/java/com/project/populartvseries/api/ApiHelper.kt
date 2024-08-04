package com.project.populartvseries.api

import com.project.populartvseries.response.PopularSeriesResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getPopularSeries(language : String, apiKey : String): Response<PopularSeriesResponse>

}