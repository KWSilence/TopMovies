package com.kwsilence.topmovies.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.databinding.FragmentSchedulingBinding
import com.kwsilence.topmovies.util.CalendarListener
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.toast
import com.kwsilence.topmovies.viewmodel.NotificationFragmentViewModel
import java.util.Calendar
import java.util.Date

class SchedulingFragment : Fragment() {
  private var _binding: FragmentSchedulingBinding? = null
  private val binding get() = _binding!!
  private val args: SchedulingFragmentArgs by navArgs()
  private lateinit var calendarListener: CalendarListener
  private val viewModel: NotificationFragmentViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSchedulingBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val movie = args.movie
    val dateParsed = DateFormatter.parseSchedule(movie.schedule)
    calendarListener = CalendarListener()
    binding.apply {
      title.text = movie.title
      date.setOnDateChangeListener(calendarListener)
      time.setIs24HourView(true)
      submitButton.setOnClickListener {
        val time = getDateTime()
        viewModel.schedule(movie, time)
      }
      if (dateParsed == null) {
        setTime()
      } else {
        date.date = dateParsed.time
        time.apply {
          hour = DateFormatter.getTime(dateParsed, Calendar.HOUR_OF_DAY)
          minute = DateFormatter.getTime(dateParsed, Calendar.MINUTE)
        }
      }
    }
    subscribeToSchedule()
    subscribeToDelete()
    setHasOptionsMenu(true)
  }

  private fun subscribeToSchedule() {
    viewModel.scheduleResult.observe(viewLifecycleOwner) { result ->
      result.onSuccess { toastAndReturn(it) }
    }
  }

  private fun subscribeToDelete() {
    viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
      result.onSuccess { toastAndReturn(it) }
    }
  }

  private fun setTime() {
    val calendar = Calendar.getInstance()
    binding.time.apply {
      hour = calendar.get(Calendar.HOUR_OF_DAY)
      minute = calendar.get(Calendar.MINUTE)
    }
  }

  private fun getDateTime(): Date {
    val hour = binding.time.hour
    val minute = binding.time.minute
    val date = calendarListener.getDate()
    val time = DateFormatter.msHour(hour) + DateFormatter.msMinute(minute)
    return when (date) {
      null -> {
        val oDate = DateFormatter.getOnlyDate(binding.date.date) ?: Date()
        Date(oDate.time + time)
      }
      else -> Date(date.time + time)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_delete_notification, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.delete_notification -> {
        val movie = args.movie
        AlertDialog.Builder(requireContext()).apply {
          setPositiveButton("Yes") { _, _ ->
            viewModel.deleteNotification(movie)
          }
          setNegativeButton("No") { _, _ -> }
          setTitle("Delete Notification?")
          setMessage("${movie.title}\n${movie.schedule}")
        }.create().show()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun toastAndReturn(msg: String) {
    requireContext().toast(msg)
    findNavController().popBackStack()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
