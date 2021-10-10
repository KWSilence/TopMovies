package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.databinding.FragmentMovieListBinding
import com.kwsilence.topmovies.state.MovieListState
import com.kwsilence.topmovies.viewmodel.MovieListViewModel

class MovieListFragment : Fragment() {
  private val viewModel: MovieListViewModel by viewModels()
  private lateinit var binding: FragmentMovieListBinding
  private lateinit var listAdapter: MovieListAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMovieListBinding.inflate(inflater, container, false)

    listAdapter = viewModel.listAdapter
    binding.movieList.adapter = listAdapter
    binding.listRefresh.setOnRefreshListener(listAdapter)

    viewModel.movies.observe(viewLifecycleOwner, { listAdapter.changeData(it) })
    listAdapter.listState.observe(
      viewLifecycleOwner, { state ->
        when (state) {
          is MovieListState.LoadMore -> viewModel.loadMoreMovie()
          is MovieListState.Refresh -> {
            binding.listRefresh.isRefreshing = false
            viewModel.refreshMovie()
          }
          else -> viewModel.idle()
        }
      }
    )

    viewModel.lossConnection.observe(
      viewLifecycleOwner, { connection ->
        if (connection) {
          Toast.makeText(
            requireContext(),
            "Please, check internet connection.",
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    )

    return binding.root
  }
}
