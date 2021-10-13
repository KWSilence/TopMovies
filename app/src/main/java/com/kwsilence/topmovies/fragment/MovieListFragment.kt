package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.adapter.pager.TitledFragment
import com.kwsilence.topmovies.databinding.FragmentMovieListBinding
import com.kwsilence.topmovies.state.MovieListState
import com.kwsilence.topmovies.viewmodel.MovieListViewModel

class MovieListFragment(title: String) : TitledFragment(title) {
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

    viewModel.movies.observe(viewLifecycleOwner, {
      Log.d("TopMovies", "onCreateView: change data")
      listAdapter.changeData(it)
    })

    listAdapter.listState.observe(
      viewLifecycleOwner, { state ->
        when (state) {
          is MovieListState.LoadMore -> {
            Log.d("TopMovies", "onCreateView: load")
            viewModel.loadMoreMovie()
          }
          is MovieListState.Refresh -> {
            Log.d("TopMovies", "onCreateView: refresh")
            binding.listRefresh.isRefreshing = false
            viewModel.refreshMovie()
          }
          else -> Unit
        }
      }
    )

    viewModel.lossConnection.observe(
      viewLifecycleOwner, { connection ->
        if (connection) {
          makeToast("Please, check internet connection.")
          viewModel.messageShown()
        }
      }
    )

    return binding.root
  }

  private fun makeToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
  }
}
