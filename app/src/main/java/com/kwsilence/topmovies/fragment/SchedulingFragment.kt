package com.kwsilence.topmovies.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.databinding.FragmentSchedulingBinding
import com.kwsilence.topmovies.util.CalendarListener
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.viewmodel.NotificationFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.Date

class SchedulingFragment : Fragment() {
  private lateinit var binding: FragmentSchedulingBinding
  private val args: SchedulingFragmentArgs by navArgs()
  private lateinit var calendarListener: CalendarListener
  private val viewModel: NotificationFragmentViewModel by viewModels()
  private val disposeBag = CompositeDisposable()

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
    binding.time.setIs24HourView(true)
    binding.submitButton.setOnClickListener {
      val time = getDateTime()
      disposeBag.add(
        viewModel.schedule(movie, time).subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            toastAndReturn("scheduled: ${it.schedule}")
          }, {
            Log.e("TopMovies", "submit schedule: ${it.localizedMessage}")
          })
      )
    }
    val date = DateFormatter.parseSchedule(movie.schedule)
    if (date == null) {
      setTime()
    } else {
      binding.date.date = date.time
      binding.time.apply {
        hour = DateFormatter.getTime(date, Calendar.HOUR_OF_DAY)
        minute = DateFormatter.getTime(date, Calendar.MINUTE)
      }
      setHasOptionsMenu(true)
    }
    return binding.root
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
    return if (date == null) {
      val oDate = DateFormatter.getOnlyDate(binding.date.date) ?: Date()
      Date(oDate.time + time)
    } else {
      Date(date.time + time)
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
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
          disposeBag.add(
            viewModel.deleteNotification(movie).subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({
                toastAndReturn("notification deleted")
              }, { e ->
                Log.e("TopMovies", "delete notification: ${e.localizedMessage}")
              })
          )
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Notification?")
        builder.setMessage("${movie.title}\n${movie.schedule}")
        builder.create().show()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun toastAndReturn(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    findNavController().popBackStack()
  }

  override fun onDestroy() {
    disposeBag.clear()
    super.onDestroy()
  }
}
