package com.project.populartvseries.resources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.ui.dataClass.MovieListItem

class SeriesPagingSource(
    private val seriesViewModel: SeriesViewModel
) : PagingSource<Int, MovieListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItem> {
        val page = params.key ?: 1
        return try {
            val response = seriesViewModel.fetchPopularSeries(language = "en-US", page = page)
            val data = response.body()?.results?.map {
                MovieListItem(
                    "https://image.tmdb.org/t/p/w500${it?.posterPath}",
                    it?.id.toString() ?: ""
                )
            } ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
