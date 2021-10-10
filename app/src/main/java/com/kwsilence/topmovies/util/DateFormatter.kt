package com.kwsilence.topmovies.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
  private val input = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val output = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)

  fun parse(date: String?): String? {
    date ?: return "not released"
    return try {
      input.parse(date)?.let { output.format(it) }
    } catch (e: Exception) {
      null
    }
  }
}
