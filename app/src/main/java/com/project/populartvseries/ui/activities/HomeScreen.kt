package com.project.populartvseries.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.project.populartvseries.ui.theme.PopularTVSeriesTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PopularTVSeriesTheme {
                HomeScreenUI()
            }
        }
    }
}

@Composable
fun HomeScreenUI() {
    Text(text = "Welcome to Popular TV Series")
}


