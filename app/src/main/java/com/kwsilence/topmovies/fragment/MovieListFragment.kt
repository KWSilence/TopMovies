package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.databinding.FragmentMovieListBinding
import com.kwsilence.topmovies.state.MovieListState
import com.kwsilence.topmovies.util.toast
import com.kwsilence.topmovies.viewmodel.MovieListViewModel

class MovieListFragment : Fragment() {
  private val viewModel: MovieListViewModel by viewModels()
  private var _binding: FragmentMovieListBinding? = null
  private val binding get() = _binding!!
  private var _listAdapter: MovieListAdapter? = null
  private val listAdapter get() = _listAdapter!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _listAdapter = viewModel.listAdapter
    binding.movieList.adapter = listAdapter
    binding.listRefresh.setOnRefreshListener(listAdapter)
    listAdapter.setDefaultScheduleText(getString(R.string.schedule_watching))

    viewModel.movies.observe(viewLifecycleOwner) {
      Log.d("TopMovies", "onCreateView: change data")
      listAdapter.changeData(it)
    }

    listAdapter.listState.observe(viewLifecycleOwner) { state ->
      when (state) {
        MovieListState.LoadMore -> {
          Log.d("TopMovies", "onCreateView: load")
          viewModel.loadMoreMovie()
        }
        MovieListState.Refresh -> {
          Log.d("TopMovies", "onCreateView: refresh")
          binding.listRefresh.isRefreshing = false
          viewModel.refreshMovie()
        }
        else -> Unit
      }
    }

    viewModel.lossConnection.observe(viewLifecycleOwner) { connection ->
      if (connection) {
        requireContext().toast("Please, check internet connection.")
        viewModel.messageShown()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
    _listAdapter = null
  }
}
