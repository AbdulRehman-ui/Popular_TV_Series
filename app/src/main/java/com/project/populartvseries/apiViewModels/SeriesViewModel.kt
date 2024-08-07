package com.project.populartvseries.apiViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.populartvseries.common.Resource
import com.project.populartvseries.repositories.SeriesRepository
import com.project.populartvseries.resources.SearchSeriesPagingSource
import com.project.populartvseries.resources.SeriesPagingSource
import com.project.populartvseries.response.PopularSeriesResponse
import com.project.populartvseries.response.ResultsItemPopular
import com.project.populartvseries.response.SearchSeriesResponse
import com.project.populartvseries.response.SeasonDetailsResponse
import com.project.populartvseries.response.SeriesDetailsResponse
import com.project.populartvseries.room.entities.PopularSeriesEntity
import com.project.populartvseries.ui.dataClass.MovieListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SeriesViewModel @Inject constructor(
    val mainRepository: SeriesRepository
)  :  ViewModel() {


    private val _res_popular_series = MutableLiveData<Resource<PopularSeriesResponse>>()
    val res_popular_series: LiveData<Resource<PopularSeriesResponse>>
        get() = _res_popular_series

    val pager = Pager(PagingConfig(pageSize = 10)) {
        SeriesPagingSource(this)
    }.flow.cachedIn(viewModelScope)

    val localSeriesData = mainRepository.getPopularSeriesFromLocal()

    fun getPopularSeries(language: String, page: Int) = viewModelScope.launch {
        _res_popular_series.postValue(Resource.loading(null))
        mainRepository.getPopularSeries(language, page, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_popular_series.postValue(Resource.success(it.body()))

                it.body()?.results?.let { results ->
                    val entities = results.map {
                        PopularSeriesEntity(
                            it?.id.toString(),
                            it?.posterPath ?: "",
                            it?.backdropPath ?: ""
                        )
                    }
                    mainRepository.popularSeriesLocal(entities)
                }

            } else {
                _res_popular_series.postValue(Resource.error(it.message(), null))
            }
        }
    }

    fun loadPopularSeriesFromLocalDb() = viewModelScope.launch {
        localSeriesData.value?.let { popularSeries ->
            val results = popularSeries.map {
                ResultsItemPopular(
                    id = it.id.toIntOrNull(),
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath
                )
            }
            val response = PopularSeriesResponse(results = results)
            _res_popular_series.postValue(Resource.success(response))
        }
    }

    suspend fun fetchPopularSeries(language: String, page: Int): Response<PopularSeriesResponse> {
        return mainRepository.getPopularSeries(language, page, "7033a297d26122cdb80b8f226ee83111")
    }


    private val _res_series_details = MutableLiveData<Resource<SeriesDetailsResponse>>()

    val res_series_details: LiveData<Resource<SeriesDetailsResponse>>
        get() = _res_series_details

    fun getSeriesDetails(seriesId : String, language : String) = viewModelScope.launch {
        _res_series_details.postValue(Resource.loading(null))
        mainRepository.getSeriesDetails(seriesId, language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_series_details.postValue(Resource.success(it.body()))
            } else {
                _res_series_details.postValue(Resource.error(it.message(), null))
            }
        }
    }

    private val _res_season_details = MutableLiveData<Resource<SeasonDetailsResponse>>()

    val res_season_details: LiveData<Resource<SeasonDetailsResponse>>
        get() = _res_season_details

    fun getSeasonDetails(seriesId : String, seasonId : String, language : String) = viewModelScope.launch {
        _res_season_details.postValue(Resource.loading(null))
        mainRepository.getSeasonDetails(seriesId, seasonId, language, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_season_details.postValue(Resource.success(it.body()))
            } else {
                _res_season_details.postValue(Resource.error(it.message(), null))
            }
        }
    }



    private val _res_search_details = MutableLiveData<Resource<SearchSeriesResponse>>()
    val res_search_details: LiveData<Resource<SearchSeriesResponse>> get() = _res_search_details

    fun getSearchSeriesPager(query: String): Flow<PagingData<MovieListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchSeriesPagingSource(mainRepository, query) }
        ).flow
    }

    fun getsearchDetails(query: String, page: Int, language: String) = viewModelScope.launch {
        _res_search_details.postValue(Resource.loading(null))
        mainRepository.getSearchSeriesDetails(query, language, page, "7033a297d26122cdb80b8f226ee83111").let {
            if (it.isSuccessful) {
                _res_search_details.postValue(Resource.success(it.body()))
            } else {
                _res_search_details.postValue(Resource.error(it.message(), null))
            }
        }
    }

}


