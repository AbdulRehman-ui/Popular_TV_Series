package com.project.populartvseries

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PopularSeriesApplication : Application() {

    companion object{
        var appContext : PopularSeriesApplication? = null
        var context : Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        context = this
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

}