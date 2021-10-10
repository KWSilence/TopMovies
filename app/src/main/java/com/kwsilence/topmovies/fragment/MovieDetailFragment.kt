package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kwsilence.topmovies.databinding.FragmentMovieDetailBinding
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.ImageLoader

class MovieDetailFragment : Fragment() {

  private lateinit var binding: FragmentMovieDetailBinding
  private val args: MovieDetailFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val movie = args.movie

    ImageLoader.setImage(binding.poster, movie.posterPath)
    binding.overview.text = movie.overview
    val progress: Double = movie.voteAverage * 10
    binding.rating.progress = progress.toFloat()
    binding.ratingValue.text = progress.toInt().toString()
    binding.title.text = movie.title
    binding.release.text = DateFormatter.parse(movie.releaseDate)
    binding.lang.text = movie.originalLang
    binding.originalTitle.text = movie.originalTitle
    binding.voteCount.text = movie.voteCount.toString()
    binding.popularity.text = movie.popularity.toString()
//    binding.scheduleButton.setOnClickListener {
//      // TODO schedule
//    }
  }
}
