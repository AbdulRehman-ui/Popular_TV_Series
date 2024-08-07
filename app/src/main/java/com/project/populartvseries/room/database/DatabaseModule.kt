package com.project.populartvseries.room.database

import android.content.Context
import androidx.room.Room
import com.project.populartvseries.room.dao.PopularSeriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun providePopularSeriesDao(database: AppDatabase): PopularSeriesDao {
        return database.popularSeriesDao()
    }
}