package com.project.populartvseries.api

import com.project.populartvseries.response.PopularSeriesResponse
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
        @Query("api_key") apiKey : String
    ): Response<PopularSeriesResponse>
}