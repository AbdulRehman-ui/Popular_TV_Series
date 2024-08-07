package com.project.populartvseries.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.populartvseries.room.dao.PopularSeriesDao
import com.project.populartvseries.room.entities.PopularSeriesEntity

@Database(entities = [PopularSeriesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularSeriesDao(): PopularSeriesDao
}