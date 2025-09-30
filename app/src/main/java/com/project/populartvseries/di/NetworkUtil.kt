package com.project.populartvseries.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat


/*
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    This line gets the ConnectivityManager, which is a system service that manages network connections.
    It's used to check the status of the device's network connectivity.
 */

/*
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    This checks if the device is running Android Marshmallow (API level 23) or higher. This is important
    because Android introduced new methods for handling network connections starting from this version.
 */

/*
    val network = connectivityManager.activeNetwork ?: return false
    * This gets the currently active network. If there is no active network, it returns false.

    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    * This retrieves the capabilities of the active network (like whether it supports Wi-Fi, cellular,
      etc.). If it can't get these capabilities, it returns false.

    return when { ... }
    * This when block checks if the active network has specific types of transport (Wi-Fi, cellular,
      Ethernet) using the hasTransport method. If any of these types are present, the function returns true.
 */

/*
    The else block handles older Android versions using deprecated methods:
        @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        It uses the deprecated activeNetworkInfo method to get the current network information. If null, it returns false.

        @Suppress("DEPRECATION") return networkInfo.isConnected
        Finally, it checks if the network is connected using the deprecated isConnected method.
 */

object NetworkUtils {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}