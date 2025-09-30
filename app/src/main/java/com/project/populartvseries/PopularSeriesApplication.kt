package com.project.populartvseries

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/*
    @HiltAndroidApp Annotation
        Purpose: This annotation is used to trigger Hilt's code generation. It sets up the
        application for dependency injection (DI) using Hilt. Hilt is a dependency injection library
        for Android that reduces the boilerplate code required for DI
 */

/*
    companion object
        Purpose: The companion object is used to define members that are shared across all instances
        of the PopularSeriesApplication class. It allows you to access appContext and context
        statically, without needing an instance of the class.
 */

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