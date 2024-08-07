package com.project.populartvseries.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.di.NetworkUtils
import com.project.populartvseries.room.entities.PopularSeriesEntity
import com.project.populartvseries.ui.common.MoviesList
import com.project.populartvseries.ui.common.MoviesListLocal
import com.project.populartvseries.ui.dataClass.BannerItem
import com.project.populartvseries.ui.dataClass.MovieListItem
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@AndroidEntryPoint
class HomeScreen : ComponentActivity() {

    val seriesViewModel: SeriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopularTVSeriesTheme {
                HomeScreenUI(seriesViewModel)
            }
        }
    }
}

@Composable
fun HomeScreenUI(seriesViewModel: SeriesViewModel) {
    val context = LocalContext.current
    val seriesResponse by seriesViewModel.res_popular_series.observeAsState()
    var movies = listOf<MovieListItem>()
    var bannerItems = listOf<BannerItem>()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val seriesPager = seriesViewModel.pager.collectAsLazyPagingItems()

    val localSeriesData by seriesViewModel.localSeriesData.observeAsState(emptyList())


    LaunchedEffect(Unit) {
        if (NetworkUtils.isOnline(context)) {
            seriesViewModel.getPopularSeries(language = "en-US", page = 1)
        } else {
            seriesViewModel.loadPopularSeriesFromLocalDb()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.app_logo_horizontal),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp)
                    .width(140.dp),
                alignment = Alignment.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(end = 20.dp, top = 20.dp)
                    .wrapContentSize()
                    .clickable {
                        context.startActivity(Intent(context, SearchScreen::class.java))
                    },
                alignment = Alignment.TopEnd,
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {

                swipeRefreshState.isRefreshing = true

                if (NetworkUtils.isOnline(context)) {
                    seriesViewModel.getPopularSeries(language = "en-US", page = 1)
                    seriesPager.refresh()
                } else {
                    seriesViewModel.loadPopularSeriesFromLocalDb()
                }

                swipeRefreshState.isRefreshing = false

                seriesPager.refresh()

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

                        bannerItems = seriesResponse!!.data?.results?.mapNotNull {
                            it?.backdropPath?.let { path ->
                                BannerItem("https://image.tmdb.org/t/p/w500$path", it.id.toString())
                            }
                        } ?: emptyList()


                        Column {

                            if (bannerItems.isNotEmpty()) {
                                BannerSlider(items = bannerItems)
                            } else {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = stringResource(R.string.popular),
                                fontSize = 19.sp,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            MoviesList(items = seriesPager) { item ->
                                println("Clicked item: ${item.seriesId}")

                                val intent = Intent(context, SeriesScreen::class.java).apply {
                                    putExtra("seriesId", item.seriesId)
                                }
                                context.startActivity(intent)
                            }

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

                    null -> {

                    }
                }
            }

            else {

                movies = localSeriesData.map {
                    MovieListItem(
                        "https://image.tmdb.org/t/p/w500${it.posterPath}",
                        it.id
                    )
                }

                bannerItems = localSeriesData.mapNotNull {
                    it.backdropPath.let { path ->
                        BannerItem("https://image.tmdb.org/t/p/w500$path", it.id)
                    }
                }

                Column {
                    if (bannerItems.isNotEmpty()) {
                        BannerSlider(items = bannerItems)
                    } else {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = stringResource(R.string.popular),
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    MoviesListLocal(items = movies) { item ->
                        println("Clicked item: ${item.seriesId}")
                        val intent = Intent(context, SeriesScreen::class.java).apply {
                            putExtra("seriesId", item.seriesId)
                        }
                        context.startActivity(intent)
                    }
                }
            }

        }
    }
}

@Composable
fun BannerSlider(items: List<BannerItem>) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(items) {
        if (items.isNotEmpty()) {
            while (true) {
                try {
                    delay(3000)
                    val nextPage = (pagerState.currentPage + 1) % items.size
                    scope.launch {
                        pagerState.animateScrollToPage(nextPage)
                    }
                } catch (e: CancellationException) {
                    break
                } catch (e: Exception) {
                    Log.e("BannerSlider", "Error during auto-scroll", e)
                }
            }
        }
    }
    HorizontalPager(
        state = pagerState,
        count = items.size,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable {
                    val intent = Intent(context, SeriesScreen::class.java).apply {
                        putExtra("seriesId", items[page].seriesId)
                    }
                    context.startActivity(intent)
                }
        ) {
            AsyncImage(
                model = items[page].bannerUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LoadingProgressUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}


@Composable
fun NoInternetUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_internet),
            contentDescription = "No Internet",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

      Text(
          text = "No Network available",
          color = MaterialTheme.colorScheme.tertiary,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
      )
    }
}

