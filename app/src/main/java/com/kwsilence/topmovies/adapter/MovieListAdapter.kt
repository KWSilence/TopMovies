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
import com.kwsilence.topmovies.fragment.MainFragmentDirections
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.state.MovieListState
import com.kwsilence.topmovies.util.DateFormatter
import com.kwsilence.topmovies.util.ImageLoader

class MovieListAdapter :
  RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(), SwipeRefreshLayout.OnRefreshListener {
  val listState = MutableLiveData(MovieListState.Default)
  private var displayedList = ArrayList<Movie>()
  private var defaultText: String = ""

  class MovieViewHolder(val binding: MovieRowBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = MovieRowBinding.inflate(inflater, parent, false)
    return MovieViewHolder(binding)
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    if (position == displayedList.size) {
      holder.binding.loading.visibility = View.VISIBLE
      holder.binding.rowContent.visibility = View.GONE
    } else {
      val currentMovie = displayedList[position]

      holder.binding.loading.visibility = View.GONE
      holder.binding.rowContent.visibility = View.VISIBLE

      ImageLoader.setImage(holder.binding.poster, currentMovie.posterPath)

      holder.binding.overview.text = currentMovie.overview
      val progress: Double = currentMovie.voteAverage * 10
      holder.binding.rating.progress = progress.toFloat()
      holder.binding.ratingValue.text = progress.toInt().toString()
      holder.binding.title.text = currentMovie.title
      holder.binding.release.text = DateFormatter.parse(currentMovie.releaseDate)
      holder.binding.scheduleButton.text = if (currentMovie.schedule != null) {
        currentMovie.schedule
      } else {
        defaultText
      }

      holder.binding.root.setOnClickListener {
        val action = MainFragmentDirections.letMovieDetail(currentMovie)
        holder.itemView.findNavController().navigate(action)
      }
      holder.binding.scheduleButton.setOnClickListener {
        val action = MainFragmentDirections.listToScheduling(currentMovie)
        holder.itemView.findNavController().navigate(action)
      }
    }
  }

  override fun getItemCount(): Int = displayedList.size + 1

  override fun onViewAttachedToWindow(holder: MovieViewHolder) {
    super.onViewAttachedToWindow(holder)
    if (holder.layoutPosition >= displayedList.size) {
      Log.d("TopMovies", "load more!!!")
      listState.value = MovieListState.LoadMore
    } else {
      if (listState.value == MovieListState.LoadMore) {
        Log.d("TopMovies", "stop loading")
        listState.value = MovieListState.Default
      }
    }
  }

  override fun onRefresh() {
    listState.value = MovieListState.Refresh
  }

  fun changeData(movies: List<Movie>?) {
    movies ?: return
    when (val state = listState.value) {
      MovieListState.Refresh, MovieListState.Default -> {
        displayedList = ArrayList(movies)
        notifyDataSetChanged()
        if (state == MovieListState.Refresh) {
          listState.value = MovieListState.Default
        }
      }
      MovieListState.LoadMore -> {
        val last = itemCount - 1
        val addSize = movies.size - last
        displayedList = ArrayList(movies)
        notifyItemRangeChanged(last, addSize)
        listState.value = MovieListState.Default
      }
      else -> return
    }
  }

  fun setDefaultScheduleText(txt: String) {
    defaultText = txt
  }
}
