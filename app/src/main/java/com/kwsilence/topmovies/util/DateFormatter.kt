package com.kwsilence.topmovies.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
  val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val format2 = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
  private val format3 = SimpleDateFormat("MMMM dd, yyyy. HH:mm", Locale.ENGLISH)

  fun parse(date: String?): String? {
    date ?: return "not released"
    return try {
      format.parse(date)?.let { format2.format(it) }
    } catch (e: Exception) {
      null
    }
  }
  fun parse(date: Date): String = format3.format(date)

  fun msHour(hour: Int): Long = hour.toLong() * 60 * 60 * 1000
  fun msMinute(minute: Int): Long = minute.toLong() * 60 * 1000

  fun getOnlyDate(time: Long): Long = format.parse(format.format(time)).time
}
