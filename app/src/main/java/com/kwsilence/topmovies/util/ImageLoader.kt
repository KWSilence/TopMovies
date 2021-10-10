package com.kwsilence.topmovies.util

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import java.lang.Exception

object ImageLoader {
  private const val baseURL = "https://image.tmdb.org"
  private const val defaultImageSize = 500

  fun getImage(path: String?, size: Int = defaultImageSize): Bitmap? {
    path ?: return null
    return try {
      Picasso.get().load("$baseURL/t/p/w$size$path").get()
    } catch (e: Exception) {
      null
    }
  }
}
