package com.project.populartvseries.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.di.NetworkUtils
import com.project.populartvseries.ui.common.MoviesList
import com.project.populartvseries.ui.dataClass.MovieListItem
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchScreen : ComponentActivity() {

    val seriesViewModel: SeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopularTVSeriesTheme {
                SearchScreenUI(seriesViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenUI(seriesViewModel: SeriesViewModel) {

    val context = LocalContext.current
    val seriesResponse by seriesViewModel.res_search_details.observeAsState()
    var movies = listOf<MovieListItem>()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    val pagingItems = seriesViewModel.getSearchSeriesPager(searchQuery.value.text).collectAsLazyPagingItems()

    LaunchedEffect(searchQuery.value.text) {
        if (searchQuery.value.text.length >= 3) {

            if(NetworkUtils.isOnline(context)) {
                seriesViewModel.getsearchDetails(searchQuery.value.text, 1, "en-US")
                pagingItems.refresh()
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary))
    {

        Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 15.dp)) {
            TextField(
                value = searchQuery.value,
                onValueChange = {
                    searchQuery.value = it
                },
                placeholder = {
                    Text(
                        stringResource(R.string.search_series_here),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = TextFieldDefaults.textFieldColors(
                    Color.Black,
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red,
                    unfocusedTextColor = Color.Black
                ),
            )
        }

        if (searchQuery.value.text.isEmpty() || searchQuery.value.text.length < 2) {
            Text(
                text = "Type something to see results",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    swipeRefreshState.isRefreshing = true
                    if(NetworkUtils.isOnline(context)) {
                        if (searchQuery.value.text.length >= 3) {
                            seriesViewModel.getsearchDetails(searchQuery.value.text, 1, "en-US")
                        }
                        pagingItems.refresh()
                    }
                    swipeRefreshState.isRefreshing = false

                }
            ) {
                if (NetworkUtils.isOnline(context)) {
                    when (seriesResponse?.status) {
                        Status.SUCCESS -> {

                            movies = seriesResponse!!.data?.results?.map {
                                MovieListItem(
                                    "https://image.tmdb.org/t/p/w500${it?.posterPath}",
                                    it?.id.toString() ?: ""
                                )
                            } ?: emptyList()

                            MoviesList(items = pagingItems) { item ->
                                val intent = Intent(context, SeriesScreen::class.java).apply {
                                    putExtra("seriesId", item.seriesId)
                                }
                                context.startActivity(intent)
                            }

                        }

                        Status.LOADING -> {
                            LoadingProgressUI()
                        }

                        Status.ERROR -> {
                            Toast.makeText(
                                context,
                                "Error: ${seriesResponse?.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        null -> {}
                    }
                }
                else {
                    NoInternetUI()
                }
            }
        }

    }
}


