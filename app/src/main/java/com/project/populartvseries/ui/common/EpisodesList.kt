package com.project.populartvseries.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.project.populartvseries.R
import com.project.populartvseries.ui.dataClass.EpisodeListItem
import kotlin.math.max

@Composable
fun EpisodesList(items: List<EpisodeListItem>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 8.dp),
    ) {
        items(items) { item ->
            EpisodeView(item)
        }
    }
}


@Composable
fun EpisodeView(item: EpisodeListItem) {
    Row(
        modifier = Modifier.padding(top = 14.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = item.episodeImage),
            contentDescription = null,
            modifier = Modifier
                .height(90.dp)
                .width(140.dp)
                .background(Color.Gray),
            contentScale = ContentScale.FillBounds,
        )

        Column (modifier = Modifier.padding(start = 8.dp)){

            Text(
                text = "${item.episodeNumber}. ${item.episodeName}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = MaterialTheme.colorScheme.tertiary,
            )

            Text(
                text = item.episodeTime,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )

            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "star",
                    modifier = Modifier
                        .size(11.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = item.episodeRating,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

        }


    }
}
