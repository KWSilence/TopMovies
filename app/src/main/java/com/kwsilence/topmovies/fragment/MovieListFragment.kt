package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.databinding.FragmentMovieListBinding
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
    binding.movieList.adapter = viewModel.listAdapter
    return binding.root
  }
}
