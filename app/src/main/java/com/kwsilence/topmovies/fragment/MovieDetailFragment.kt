package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kwsilence.topmovies.databinding.FragmentMovieDetailBinding
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.ImageLoader

class MovieDetailFragment : Fragment() {

  private var _binding: FragmentMovieDetailBinding? = null
  private val binding get() = _binding!!
  private val args: MovieDetailFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val movie = args.movie

    ImageLoader.setImage(binding.poster, movie.posterPath)
    val progress: Double = movie.voteAverage * 10
    binding.apply {
      overview.text = movie.overview
      rating.progress = progress.toFloat()
      ratingValue.text = progress.toInt().toString()
      title.text = movie.title
      release.text = DateFormatter.parse(movie.releaseDate)
      lang.text = movie.originalLang
      originalTitle.text = movie.originalTitle
      voteCount.text = movie.voteCount.toString()
      popularity.text = movie.popularity.toString()
      movie.schedule?.let { scheduleButton.text = it }
      scheduleButton.setOnClickListener {
        val action = MovieDetailFragmentDirections.detailToScheduling(movie)
        findNavController().navigate(action)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
