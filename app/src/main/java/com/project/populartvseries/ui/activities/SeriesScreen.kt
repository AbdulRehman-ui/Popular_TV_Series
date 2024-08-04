package com.project.populartvseries.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.project.populartvseries.R
import com.project.populartvseries.apiViewModels.SeriesViewModel
import com.project.populartvseries.common.Status
import com.project.populartvseries.ui.common.MoviesList
import com.project.populartvseries.ui.dataClass.BannerItem
import com.project.populartvseries.ui.dataClass.MovieListItem
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        LaunchedEffect(Unit) {
            seriesViewModel.getSeriesDetails(seriesId = seriesId, language = "en-US")
        }

        when (seriesResponse?.status) {
            Status.SUCCESS -> {

                var seriesName  = ""
                var seriesDescription  = ""
                var posterPath  = ""
                var rating  = ""
                var genreList = ""

                seriesResponse?.data?.let { it ->
                    seriesName = it.name.toString()
                    seriesDescription = it.overview.toString()
                    posterPath = "https://image.tmdb.org/t/p/w500/${it.posterPath.toString()}"
                    rating = String.format("%.1f", it.voteAverage)
                    val genresList: ArrayList<String> = it.genres
                        ?.filterNotNull()
                        ?.map { genre -> genre.name ?: "" }
                        ?.filter { it.isNotEmpty() }
                        ?.toCollection(ArrayList()) ?: arrayListOf()

                    genreList =  genresList.joinToString(" | ")

                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back arrow",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth().height(270.dp)) {
                        Image(
                            painter = rememberImagePainter(data = posterPath),
                            contentDescription = "Series Poster",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(270.dp)
                                .width(170.dp)
                                .padding(start = 10.dp)
                        )

                        Column (
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
                                    text = rating,
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
                }

            }
            Status.LOADING -> {
                LoadingProgressUI()
            }
            Status.ERROR -> {
                Toast.makeText(context, "Error: ${seriesResponse?.message}", Toast.LENGTH_SHORT).show()
            }
            null -> {

            }
        }
    }
}


