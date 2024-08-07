package com.project.populartvseries.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.di.NetworkUtils
import com.project.populartvseries.ui.common.EpisodesList
import com.project.populartvseries.ui.dataClass.EpisodeListItem
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonScreen : ComponentActivity() {

    val seriesViewModel: SeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seriesId = intent.getStringExtra("seriesId").toString()
        val seasonNumber = intent.getStringExtra("seasonNumber").toString()

        setContent {
            PopularTVSeriesTheme {
                SeasonScreenUI(seriesViewModel, seriesId, seasonNumber)
            }
        }
    }
}

@Composable
fun SeasonScreenUI(seriesViewModel: SeriesViewModel, seriesId : String, seasonNumber : String) {

    val context = LocalContext.current
    val seasonResponse by seriesViewModel.res_season_details.observeAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        LaunchedEffect(Unit) {
            if (NetworkUtils.isOnline(context)) {
                seriesViewModel.getSeasonDetails(seriesId = seriesId, seasonId = seasonNumber, language = "en-US")
            }
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                swipeRefreshState.isRefreshing = true

                if (NetworkUtils.isOnline(context)) {
                    seriesViewModel.getSeriesDetails(seriesId = seriesId, language = "en-US")
                    seriesViewModel.getSeasonDetails(seriesId = seriesId, seasonId = "1", language = "en-US")
                }
                swipeRefreshState.isRefreshing = false
            }
        ) {

            if (NetworkUtils.isOnline(context)) {
                when (seasonResponse?.status) {
                    Status.SUCCESS -> {

                        var seriesName = ""
                        var episodeTime = ""
                        var episodeRating = ""
                        var episodeList = listOf<EpisodeListItem>()

                        seasonResponse?.data.let { it ->


                            seriesName = it?.name.toString()

                            episodeList = it?.episodes?.map {

                                episodeTime = if (it?.runtime == null) {
                                    "Not Mentioned"
                                } else {
                                    it.runtime.toString() + "Mins"
                                }

                                episodeRating = if (it?.voteAverage == 0.0) {
                                    "No rating yet"
                                } else {
                                    String.format("%.1f", it?.voteAverage)
                                }

                                EpisodeListItem(
                                    "https://image.tmdb.org/t/p/w500${it?.stillPath}",
                                    it?.name.toString(),
                                    episodeTime,
                                    episodeRating,
                                    it?.episodeNumber.toString()
                                )
                            } ?: emptyList()
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 15.dp, end = 15.dp, top = 15.dp)
                        )
                        {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Back arrow",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(start = 10.dp)
                                        .clickable {
                                            (context as SeasonScreen).finish()
                                        }
                                )

                                Text(
                                    text = seriesName,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 7.dp)
                                )
                            }

                            EpisodesList(items = episodeList)

                        }

                    }

                    Status.LOADING -> {
                        LoadingProgressUI()
                    }

                    Status.ERROR -> {

                    }

                    null -> {

                    }
                }
            }
            else {
                NoInternetUI()
            }

        }
    }
}