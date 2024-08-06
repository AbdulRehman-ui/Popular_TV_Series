package com.project.populartvseries.api

import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("language") language : String,
        @Query("page") page: Int,
        @Query("api_key") apiKey : String,
    ): Response<PopularSeriesResponse>

    @GET("tv/{id}")
    suspend fun getSeriesDetails(
        @Path("id") seriesId: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ) : Response<SeriesDetailsResponse>


    @GET("tv/{series_id}/season/{season_id}")
    suspend fun getSeasonDetails(
        @Path("series_id") seriesId: String,
        @Path("season_id") seasonId: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ) : Response<SeasonDetailsResponse>

    @GET("search/tv")
    suspend fun getSearchSeriesDetails(
        @Query("query") query : String,
        @Query("language") language : String,
        @Query("page") page : Int,
        @Query("api_key") apiKey : String
    ): Response<SearchSeriesResponse>


}