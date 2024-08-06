package com.project.populartvseries.resources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.populartvseries.repositories.SeriesRepository
import com.project.populartvseries.ui.dataClass.MovieListItem

class SearchSeriesPagingSource(
    private val mainRepository: SeriesRepository,
    private val query: String
) : PagingSource<Int, MovieListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItem> {
        return try {
            val page = params.key ?: 1
            val response = mainRepository.getSearchSeriesDetails(query, "en-US", page, "7033a297d26122cdb80b8f226ee83111")
            val data = response.body()?.results?.map {
                MovieListItem(
                    "https://image.tmdb.org/t/p/w500${it?.posterPath}",
                    it?.id.toString() ?: ""
                )
            } ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListItem>): Int? {
        return null
    }
}
