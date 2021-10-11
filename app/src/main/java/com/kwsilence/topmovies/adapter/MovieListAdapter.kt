package com.kwsilence.topmovies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kwsilence.topmovies.databinding.MovieRowBinding
import com.kwsilence.topmovies.fragment.MovieListFragmentDirections
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.state.MovieListState
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.ImageLoader

class MovieListAdapter() :
  RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(), SwipeRefreshLayout.OnRefreshListener {
  val listState = MutableLiveData<MovieListState>(MovieListState.Default)
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

      ImageLoader.setImage(holder.binding.poster, currentMovie.posterPath)

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
      holder.binding.scheduleButton.setOnClickListener {
        val action = MovieListFragmentDirections.listToScheduling(currentMovie)
        holder.itemView.findNavController().navigate(action)
      }
    }
  }

  override fun getItemCount(): Int = movieList.size + 1

  override fun onViewAttachedToWindow(holder: MovieViewHolder) {
    super.onViewAttachedToWindow(holder)
    if (holder.layoutPosition >= movieList.size) {
      Log.d("TopMovies", "load more!!!")
      listState.value = MovieListState.LoadMore
    } else {
      if (listState.value is MovieListState.LoadMore) {
        Log.d("TopMovies", "stop loading")
        listState.value = MovieListState.Default
      }
    }
  }

  fun changeData(movies: List<Movie>?) {
    movies ?: return
    if (listState.value is MovieListState.Refresh) {
      if (movies.isEmpty()) {
        movieList = ArrayList(movies)
        notifyDataSetChanged()
      }
      listState.value = MovieListState.Default
    } else {
      val last = itemCount - 1
      movieList.addAll(movies)
      notifyItemRangeChanged(last, movies.size)
    }
  }

  override fun onRefresh() {
    listState.value = MovieListState.Refresh
  }
}
