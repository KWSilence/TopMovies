package com.kwsilence.topmovies.util

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.lang.Exception

object ImageLoader {
  private const val baseURL = "https://image.tmdb.org"
  private const val defaultImageSize = 500

  fun setImage(view: ImageView, path: String?, size: Int = defaultImageSize) {
    path ?: return
    try {
      Picasso.get().load("$baseURL/t/p/w$size$path").into(view)
    } catch (e: Exception) {
      Log.e("TopMovies", "ImageLoader.setImage: ${e.localizedMessage}")
    }
  }
}
