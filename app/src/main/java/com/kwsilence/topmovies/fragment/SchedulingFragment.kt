package com.kwsilence.topmovies.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kwsilence.topmovies.databinding.FragmentSchedulingBinding
import com.kwsilence.topmovies.util.CalendarListener
import com.kwsilence.topmovies.util.DateFormatter
import java.util.Calendar
import java.util.Date

class SchedulingFragment : Fragment() {
  private lateinit var binding: FragmentSchedulingBinding
  private val args: SchedulingFragmentArgs by navArgs()
  private lateinit var calendarListener: CalendarListener

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentSchedulingBinding.inflate(inflater, container, false)
    val movie = args.movie
    calendarListener = CalendarListener()
    binding.title.text = movie.title
    binding.date.setOnDateChangeListener(calendarListener)
    binding.submitButton.setOnClickListener {
      val time = DateFormatter.parse(getDateTime())
      Toast.makeText(requireContext(), time, Toast.LENGTH_SHORT).show()
    }
    setTime()
    return binding.root
  }

  private fun setTime() {
    val calendar = Calendar.getInstance()
    binding.time.apply {
      setIs24HourView(true)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
      } else {
        currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        currentMinute = calendar.get(Calendar.MINUTE)
      }
    }
  }

  private fun getDateTime(): Date {
    var hour = 0
    var minute = 0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      hour = binding.time.hour
      minute = binding.time.minute
    } else {
      hour = binding.time.currentHour
      minute = binding.time.currentMinute
    }
    val date = calendarListener.getDate()
    val time = DateFormatter.msHour(hour) + DateFormatter.msMinute(minute)
    return if (date == null) {
      Date(DateFormatter.getOnlyDate(binding.date.date) + time)
    } else {
      Date(date + time)
    }
  }
}
