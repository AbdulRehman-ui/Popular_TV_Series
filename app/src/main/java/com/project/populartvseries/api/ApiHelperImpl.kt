package com.project.populartvseries.api


import com.project.populartvseries.response.PopularSeriesResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getPopularSeries(language : String, apiKey : String): Response<PopularSeriesResponse> =
        apiService.getPopularSeries(language, apiKey)

}