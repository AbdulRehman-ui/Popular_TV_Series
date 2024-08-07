package com.project.populartvseries.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.populartvseries.room.entities.PopularSeriesEntity

@Dao
interface PopularSeriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularSeries(series: List<PopularSeriesEntity>)

    @Query("SELECT * FROM popular_series")
    fun getPopularSeries(): LiveData<List<PopularSeriesEntity>>
}