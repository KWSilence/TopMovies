package com.kwsilence.topmovies.fragment

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
import com.kwsilence.topmovies.R
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

    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_toggle_list, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.toggle_list -> {
        val toggleState = listAdapter.toggleLists()
        binding.listRefresh.isEnabled = !toggleState
        if (toggleState && listAdapter.getScheduleCount() == 0) {
          makeToast("No schedule yet")
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun makeToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
  }
}
