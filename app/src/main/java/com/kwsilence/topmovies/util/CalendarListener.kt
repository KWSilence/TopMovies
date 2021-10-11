package com.kwsilence.topmovies.util

import android.widget.CalendarView
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarListener() : CalendarView.OnDateChangeListener {
  private var date = ""
  private val parser = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

  fun getDate(): Long? {
    return try {
      parser.parse(date).time
    } catch (e: Exception) {
      null
    }
  }

  override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
    date = "$dayOfMonth.${month + 1}.$year"
  }
}
