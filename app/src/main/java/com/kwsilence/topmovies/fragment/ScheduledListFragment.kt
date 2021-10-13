package com.kwsilence.topmovies.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.adapter.ScheduledListAdapter
import com.kwsilence.topmovies.adapter.pager.TitledFragment
import com.kwsilence.topmovies.databinding.FragmentScheduledListBinding
import com.kwsilence.topmovies.viewmodel.NotificationFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ScheduledListFragment(title: String) : TitledFragment(title) {
  private lateinit var binding: FragmentScheduledListBinding
  private val viewModel: NotificationFragmentViewModel by viewModels()
  private lateinit var adapter: ScheduledListAdapter
  private val disposeBag = CompositeDisposable()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentScheduledListBinding.inflate(inflater, container, false)
    adapter = viewModel.listAdapter
    binding.scheduledList.adapter = adapter
    adapter.setNothingToShowText(getString(R.string.nothing_to_show_message))
    viewModel.scheduledMovies.observe(viewLifecycleOwner, { list -> adapter.setData(list) })
    adapter.notificationToDelete.observe(viewLifecycleOwner, { movie ->
      movie?.let {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
          disposeBag.add(
            viewModel.deleteNotification(movie).subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({
                makeToast("${movie.title}\nnotification deleted")
              }, { e ->
                Log.e("TopMovies", "delete notification in list: ${e.localizedMessage}")
              })
          )
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Notification?")
        builder.setMessage("${movie.title}\n${movie.schedule}")
        builder.create().show()
        adapter.resetNotificationToDelete()
      }
    })
    return binding.root
  }

  private fun makeToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
  }

  override fun onDestroy() {
    disposeBag.clear()
    super.onDestroy()
  }
}
