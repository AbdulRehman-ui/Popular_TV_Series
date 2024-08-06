package com.project.populartvseries.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.ui.common.CastList
import com.project.populartvseries.ui.common.MoviesList
import com.project.populartvseries.ui.common.SeasonList
import com.project.populartvseries.ui.dataClass.BannerItem
import com.project.populartvseries.ui.dataClass.CastIem
import com.project.populartvseries.ui.dataClass.MovieListItem
import com.project.populartvseries.ui.dataClass.SeasonListItem
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeriesScreen : ComponentActivity() {

    val seriesViewModel: SeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seriesId = intent.getStringExtra("seriesId").toString()

        setContent {
            PopularTVSeriesTheme {
                SeriesDetailsUI(seriesViewModel, seriesId)
            }
        }
    }
}

@Composable
fun SeriesDetailsUI(seriesViewModel: SeriesViewModel, seriesId : String) {

    val context = LocalContext.current
    val seriesResponse by seriesViewModel.res_series_details.observeAsState()
    val seasonResponse by seriesViewModel.res_season_details.observeAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LaunchedEffect(Unit) {
            seriesViewModel.getSeriesDetails(seriesId = seriesId, language = "en-US")
            seriesViewModel.getSeasonDetails(seriesId = seriesId, seasonId = "1", language = "en-US")
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                swipeRefreshState.isRefreshing = true
                seriesViewModel.getSeriesDetails(seriesId = seriesId, language = "en-US")
                seriesViewModel.getSeasonDetails(seriesId = seriesId, seasonId = "1", language = "en-US")
                swipeRefreshState.isRefreshing = false
            }
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                when (seriesResponse?.status) {
                    Status.SUCCESS -> {
                        seriesResponse?.data?.let { it ->
                            val seriesName = it.name.toString()
                            val seriesDescription = it.overview.toString()
                            val posterPath =
                                "https://image.tmdb.org/t/p/w500/${it.posterPath.toString()}"
                            val rating = String.format("%.1f", it.voteAverage)
                            val seasonCount = it.numberOfSeasons.toString()
                            val languagesCount = it.languages?.size.toString() ?: ""
                            val ratingCount = it.voteCount.toString()

                            val genresList: List<String> =
                                it.genres?.map { genre -> genre?.name ?: "" } ?: emptyList()
                            val genreList = genresList.joinToString(" | ")

                            val platforms = it.networks?.map {
                                CastIem(
                                    "https://image.tmdb.org/t/p/w500${it?.logoPath}",
                                    it?.name.toString() ?: ""
                                )
                            } ?: emptyList()

                            val seasons = it.seasons?.map {
                                val seasonPosterPath = if (!it?.posterPath.isNullOrEmpty()) {
                                    "https://image.tmdb.org/t/p/w500${it?.posterPath}"
                                } else {
                                    posterPath
                                }

                                SeasonListItem(
                                    seasonPosterPath,
                                    it?.name.toString() ?: "",
                                    it?.seasonNumber.toString() ?: ""
                                )
                            } ?: emptyList()

                            item {
                                Spacer(modifier = Modifier.height(10.dp))

                                Image(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Back arrow",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(start = 10.dp)
                                        .clickable {
                                            (context as SeriesScreen).finish()
                                        }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(270.dp)
                                ) {
                                    Image(
                                        painter = rememberImagePainter(data = posterPath),
                                        contentDescription = "Series Poster",
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier
                                            .height(270.dp)
                                            .width(170.dp)
                                            .padding(start = 10.dp)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 8.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = seriesName,
                                            color = MaterialTheme.colorScheme.tertiary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            maxLines = 1,
                                        )

                                        Row {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_star),
                                                contentDescription = "star",
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .align(Alignment.CenterVertically)
                                            )

                                            Text(
                                                text = "$rating ($ratingCount)",
                                                color = MaterialTheme.colorScheme.tertiary,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                modifier = Modifier.padding(start = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = seriesDescription,
                                            color = MaterialTheme.colorScheme.tertiary,
                                            fontSize = 12.sp,
                                            maxLines = 9,
                                            overflow = TextOverflow.Ellipsis,
                                            style = TextStyle(
                                                lineHeight = 20.sp
                                            )
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .padding(bottom = 5.dp)
                                                .horizontalScroll(rememberScrollState()),
                                            contentAlignment = Alignment.BottomStart
                                        ) {
                                            Text(
                                                text = genreList,
                                                color = MaterialTheme.colorScheme.tertiary,
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "$languagesCount Languages",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 50.dp)
                                )

                                Spacer(modifier = Modifier.height(15.dp))

                                Text(
                                    text = stringResource(R.string.platforms_to_watch),
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                )

                                CastList(items = platforms)

                                Spacer(modifier = Modifier.height(15.dp))

                                Text(
                                    text = "Seasons ($seasonCount)",
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                )

                                SeasonList(items = seasons) { item ->

                                    val intent = Intent(context, SeasonScreen::class.java).apply {
                                        putExtra("seasonNumber", item.seasonNumber)
                                        putExtra("seriesId", seriesId)
                                    }
                                    context.startActivity(intent)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = stringResource(R.string.cast),
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                        }
                    }

                    Status.LOADING -> {
                        item {
                            LoadingProgressUI()
                        }
                    }

                    Status.ERROR -> {
                        item {
                            Toast.makeText(
                                context,
                                "Error: ${seriesResponse?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    null -> {

                    }
                }

                when (seasonResponse?.status) {
                    Status.SUCCESS -> {
                        val castList = seasonResponse?.data?.episodes?.get(0)?.guestStars?.map {
                            CastIem(
                                "https://image.tmdb.org/t/p/w500${it?.profilePath}",
                                it?.name.toString() ?: ""
                            )
                        } ?: emptyList()

                        item {

                            if (castList.isEmpty()) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.height(100.dp).fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.nothing_to_show),
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                }
                            } else {
                                CastList(items = castList)
                            }

                        }
                    }

                    Status.LOADING -> {

                    }

                    Status.ERROR -> {
                        item {
                            Toast.makeText(
                                context,
                                "Error: ${seasonResponse?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    null -> {
                    }
                }
            }
        }
    }
}


