package com.project.populartvseries.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.project.populartvseries.ui.activities.LoadingProgressUI
import com.project.populartvseries.ui.dataClass.MovieListItem

@Composable
fun MoviesList(items: LazyPagingItems<MovieListItem>, onItemClicked: (MovieListItem) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 8.dp),
    ) {
        items(items.itemCount) { index ->
            val item = items[index]
            item?.let {
                ItemView(it, onItemClicked)
            }
        }

        items.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item(span = { GridItemSpan(3) }) {
                            LoadingProgressUI()
                    }
                }
                loadState.append is LoadState.Error -> {
                    item(span = { GridItemSpan(3) }) {
                        Text(
                            text = "Error loading more items",
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(item: MovieListItem, onItemClicked: (MovieListItem) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClicked(item) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.seriesPoster,
                contentDescription = null,
                modifier = Modifier
                    .width(140.dp)
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
