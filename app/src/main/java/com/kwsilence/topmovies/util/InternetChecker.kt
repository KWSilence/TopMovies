package com.kwsilence.topmovies.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object InternetChecker {
  private var connectivityManager: ConnectivityManager? = null
  fun setConnectivityManager(connectivityManager: ConnectivityManager) {
    this.connectivityManager = connectivityManager
  }

  fun checkInternetConnection(): Boolean {
    if (connectivityManager == null) {
      Log.e("TopMovies", "checkInternetConnection: connectivity manager don't set")
      return false
    }
    connectivityManager?.let { manager ->
      val nw = manager.activeNetwork ?: return false
      val actNw = manager.getNetworkCapabilities(nw) ?: return false
      return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
      }
    }
    return false
  }
}
