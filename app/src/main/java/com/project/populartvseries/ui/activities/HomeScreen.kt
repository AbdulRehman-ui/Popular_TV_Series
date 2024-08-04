package com.project.populartvseries.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.ui.common.MoviesList
import com.project.populartvseries.ui.dataClass.MovieListItem
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

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
    var bannerImages = listOf<String>()

    LaunchedEffect(Unit) {
        seriesViewModel.getPopularSeries(language = "en-US")
    }

    when (seriesResponse?.status) {
        Status.SUCCESS -> {
            movies = seriesResponse!!.data?.results?.map {
                MovieListItem(
                    "https://image.tmdb.org/t/p/w500${it?.posterPath}",
                    it?.id.toString() ?: ""
                )
            } ?: emptyList()

            bannerImages = seriesResponse!!.data?.results?.mapNotNull {
                it?.backdropPath?.let { path -> "https://image.tmdb.org/t/p/w500$path" }
            } ?: emptyList()
        }
        Status.LOADING -> {
            CircularProgressIndicator()
        }
        Status.ERROR -> {
            Toast.makeText(context, "Error: ${seriesResponse?.message}", Toast.LENGTH_SHORT).show()
        }
        null -> {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
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
                    },
                alignment = Alignment.TopEnd,
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        if (bannerImages.isNotEmpty()) {
            BannerSlider(images = bannerImages)
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

        MoviesList(items = movies) { item ->
            println("Clicked item: ${item.seriesId}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.padding(horizontal = 10.dp)) {
        TextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
            },
            placeholder = { Text(stringResource(R.string.search_series_here), fontSize = 14.sp, color = Color.DarkGray) },
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
}

@Composable
fun BannerSlider(images: List<String>) {
    val pagerState = rememberPagerState()

    LaunchedEffect(images) {
        if (images.isNotEmpty()) {
            while (true) {
                try {
                    delay(3000)
                    val nextPage = (pagerState.currentPage + 1) % images.size
                    pagerState.animateScrollToPage(nextPage)
                } catch (e: Exception) {
                    Log.e("BannerSlider", "Error during auto-scroll", e)
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        count = images.size,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
