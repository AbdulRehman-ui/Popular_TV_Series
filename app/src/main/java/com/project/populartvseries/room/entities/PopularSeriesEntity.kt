package com.project.populartvseries.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_series")
data class PopularSeriesEntity(
    @PrimaryKey val id : String,
    val posterPath: String,
    val backdropPath: String
)
