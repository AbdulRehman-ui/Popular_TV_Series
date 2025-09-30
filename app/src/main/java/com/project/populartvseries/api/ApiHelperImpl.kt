package com.project.populartvseries.api


import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import retrofit2.Response
import javax.inject.Inject

/*
    * ApiHelperImpl is a concrete implementation of the ApiHelper interface. It takes the ApiService
      as a dependency and delegates API calls to it.

    * The ApiHelperImpl class encapsulates the logic related to making API calls, such as passing
      parameters and handling responses.

    * By using this pattern, the SeriesRepository only depends on the ApiHelper interface, not on
      the specific implementation or the ApiService. This allows for easier testing and swapping out
      implementations if needed.
 */

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getPopularSeries(language : String, page: Int, apiKey : String): Response<PopularSeriesResponse> =
        apiService.getPopularSeries(language, page, apiKey)

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
        page: Int,
        apiKey: String
    ): Response<SearchSeriesResponse> =
        apiService.getSearchSeriesDetails(query, language, page, apiKey)

}