package com.kwsilence.topmovies.util

import android.widget.CalendarView
import java.util.Date

class CalendarListener() : CalendarView.OnDateChangeListener {
  private var date = ""

  fun getDate(): Date? {
    return try {
      DateFormatter.format.parse(date)
    } catch (e: Exception) {
      null
    }
  }

  override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
    date = "$year-${month + 1}-$dayOfMonth"
  }
}
