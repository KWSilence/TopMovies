package com.kwsilence.topmovies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kwsilence.topmovies.databinding.MovieRowBinding
import com.kwsilence.topmovies.fragment.MovieListFragmentDirections
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.ImageLoader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListAdapter(private val loadMoreMovie: Single<ArrayList<Movie>>) :
  RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
  private var movieList = ArrayList<Movie>()

  class MovieViewHolder(val binding: MovieRowBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = MovieRowBinding.inflate(inflater, parent, false)
    return MovieViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    if (position == movieList.size) {
      holder.binding.loading.visibility = View.VISIBLE
      holder.binding.rowContent.visibility = View.GONE
    } else {
      val currentMovie = movieList[position]

      holder.binding.loading.visibility = View.GONE
      holder.binding.rowContent.visibility = View.VISIBLE

      CoroutineScope(Dispatchers.IO).launch {
        val img = ImageLoader.getImage(currentMovie.posterPath)
        withContext(Dispatchers.Main) {
          holder.binding.poster.setImageBitmap(img)
        }
      }

      holder.binding.overview.text = currentMovie.overview
      val progress: Double = currentMovie.voteAverage * 10
      holder.binding.rating.progress = progress.toFloat()
      holder.binding.ratingValue.text = progress.toInt().toString()
      holder.binding.title.text = currentMovie.title
      holder.binding.release.text = DateFormatter.parse(currentMovie.releaseDate)
      holder.binding.root.setOnClickListener {
        val action = MovieListFragmentDirections.letMovieDetail(currentMovie)
        holder.itemView.findNavController().navigate(action)
      }
//    holder.binding.scheduleButton.setOnClickListener {
//      // TODO schedule
//    }
    }
  }

  override fun getItemCount(): Int = movieList.size + 1

  override fun onViewAttachedToWindow(holder: MovieViewHolder) {
    super.onViewAttachedToWindow(holder)
    if (holder.layoutPosition >= movieList.size) {
      loadMoreMovie.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ movies ->
          addData(movies)
        }, {
          Log.e("TopMovies", "movieListAdapter: ${it.localizedMessage}")
        })
    }
  }

  private fun setData(movies: ArrayList<Movie>) {
    movieList = movies
    notifyDataSetChanged()
  }

  private fun addData(movies: ArrayList<Movie>) {
    val last = itemCount - 1
    movieList.addAll(movies)
    notifyItemRangeChanged(last, movies.size)
  }
}
