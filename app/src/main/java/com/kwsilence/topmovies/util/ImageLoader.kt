package com.kwsilence.topmovies.util

import android.util.Log
import android.widget.ImageView
import coil.load

object ImageLoader {
  private const val baseURL = "https://image.tmdb.org"
  private const val defaultImageSize = 500

  fun setImage(view: ImageView, path: String?, size: Int = defaultImageSize) {
    path ?: return
    try {
      view.load("$baseURL/t/p/w$size$path")
    } catch (e: Exception) {
      Log.e("TopMovies", "ImageLoader.setImage: ${e.localizedMessage}")
    }
  }
}
