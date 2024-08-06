package com.project.populartvseries.api


import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getPopularSeries(language : String, apiKey : String): Response<PopularSeriesResponse> =
        apiService.getPopularSeries(language, apiKey)

    override suspend fun getSeriesDetails(seriesId: String, language: String, apiKey: String): Response<SeriesDetailsResponse> =
        apiService.getSeriesDetails(seriesId, language, apiKey)

    override suspend fun getSeasonDetails(
        seriesId: String,
        seasonId: String,
        language: String,
        apiKey: String
    ): Response<SeasonDetailsResponse> =
        apiService.getSeasonDetails(seriesId, seasonId, language, apiKey)

    override suspend fun getSearchSeasonDetails(
        query: String,
        language: String,
        apiKey: String
    ): Response<SearchSeriesResponse> =
        apiService.getSearchSeriesDetails(query, language, apiKey)

}