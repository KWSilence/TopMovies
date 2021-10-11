package com.kwsilence.topmovies.util

import android.widget.CalendarView

class CalendarListener() : CalendarView.OnDateChangeListener {
  private var date = ""

  fun getDate(): Long? {
    return try {
      DateFormatter.format.parse(date).time
    } catch (e: Exception) {
      null
    }
  }

  override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
    date = "$dayOfMonth.${month + 1}.$year"
  }
}
