package com.project.populartvseries.api

import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getPopularSeries(language : String, apiKey : String) : Response<PopularSeriesResponse>

    suspend fun getSeriesDetails(seriesId : String, language: String, apiKey: String) : Response<SeriesDetailsResponse>

    suspend fun getSeasonDetails(seriesId : String, seasonId : String, language: String, apiKey: String) : Response<SeasonDetailsResponse>

    suspend fun getSearchSeasonDetails(query : String, language: String, apiKey: String) : Response<SearchSeriesResponse>

}