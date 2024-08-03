package com.project.populartvseries.api

import retrofit2.http.Query
import com.project.populartvseries.response.PopularSeriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("language") language : String,
        @Query("api_key") api_key : String)
    : Response<PopularSeriesResponse>

}