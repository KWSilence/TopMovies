package com.kwsilence.topmovies.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.adapter.ScheduledListAdapter
import com.kwsilence.topmovies.databinding.FragmentScheduledListBinding
import com.kwsilence.topmovies.util.toast
import com.kwsilence.topmovies.viewmodel.NotificationFragmentViewModel

class ScheduledListFragment : Fragment() {
  private var _binding: FragmentScheduledListBinding? = null
  private val binding get() = _binding!!
  private val viewModel: NotificationFragmentViewModel by viewModels()
  private lateinit var adapter: ScheduledListAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentScheduledListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = viewModel.listAdapter
    binding.scheduledList.adapter = adapter
    adapter.setNothingToShowText(getString(R.string.nothing_to_show_message))
    viewModel.scheduledMovies.observe(viewLifecycleOwner) { list -> adapter.setData(list) }
    adapter.notificationToDelete.observe(viewLifecycleOwner) { movie ->
      movie?.let {
        AlertDialog.Builder(requireContext()).apply {
          setPositiveButton("Yes") { _, _ ->
            viewModel.deleteNotification(movie)
          }
          setNegativeButton("No") { _, _ -> }
          setTitle("Delete Notification?")
          setMessage("${movie.title}\n${movie.schedule}")
        }.create().show()
        adapter.resetNotificationToDelete()
      }
    }
    subscribeToDelete()
  }

  private fun subscribeToDelete() {
    viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
      result.onSuccess { requireContext().toast(it) }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
