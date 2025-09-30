package com.project.populartvseries.di

import com.project.populartvseries.api.ApiHelper
import com.project.populartvseries.api.ApiHelperImpl
import com.project.populartvseries.api.ApiService
import com.project.populartvseries.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/*
        Dagger Hilt is a dependency injection library that simplifies the process of providing
        dependencies in Android apps. In this code, it is used to inject dependencies like OkHttpClient,
        Retrofit, ApiService, and ApiHelperImpl.
 */


/*
        The NetworkModule is annotated with @Module, which indicates that it provides dependencies.
        The @InstallIn(SingletonComponent::class) annotation specifies that the provided
        dependencies will be available in the Singleton scope throughout the application's lifecycle.
 */


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{


    /*
        The @Provides annotation indicates methods that provide instances of dependencies.
        @Singleton ensures that a single instance of the provided dependency is used throughout the app.
     */

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    /*
        An Interceptor is used to modify the request or response before it reaches the server
        or after it is received. In this case, an interceptor adds headers (Accept and Content-Type)
        to every request.
     */


    @Provides
    fun provideHeaderIntercept() = run {
        Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest);
        }
    }


    /*
          OkHttp is a powerful HTTP client for handling network requests. It supports features like
          connection pooling, caching, and retries.
     */


    /*
          When BuildConfig.DEBUG is true, a HttpLoggingInterceptor is added to log the details of
          network requests and responses (such as headers and bodies). This is helpful during
          development and debugging.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(provideHeaderIntercept())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }else{
        OkHttpClient.Builder()
            .addInterceptor(provideHeaderIntercept())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    /*
          Retrofit is a type-safe HTTP client that converts API responses into Kotlin objects using
          a converter (e.g., Gson).

          provideRetrofit
          The provideRetrofit method configures and returns a Retrofit instance with a base URL and
          an OkHttpClient. The GsonConverterFactory is used to convert JSON responses into Kotlin
          data classes.
     */

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit {
        okHttpClient.newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}