package com.kwsilence.topmovies.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateFormatter {
  val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
  private val format2 = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
  private val format3 = SimpleDateFormat("yyyy.MM.dd, HH:mm", Locale.ENGLISH)
  private val calendar = Calendar.getInstance()

  fun parse(date: String?): String? {
    date ?: return "not released"
    return try {
      format.parse(date)?.let { format2.format(it) }
    } catch (e: Exception) {
      null
    }
  }

  fun parse(date: Date): String = format3.format(date)
  fun parseSchedule(date: String?): Date? {
    date ?: return null
    return try {
      format3.parse(date)
    } catch (e: Exception) {
      null
    }
  }

  fun msHour(hour: Int): Long = TimeUnit.HOURS.toMillis(hour.toLong())
  fun msMinute(minute: Int): Long = TimeUnit.MINUTES.toMillis(minute.toLong())
  fun getTime(date: Date, field: Int): Int {
    calendar.time = date
    return calendar.get(field)
  }

  fun getOnlyDate(time: Long): Date? = format.parse(format.format(time))
}
