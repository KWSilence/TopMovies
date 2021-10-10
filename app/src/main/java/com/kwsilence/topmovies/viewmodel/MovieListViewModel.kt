package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.api.RetrofitInstance
import com.kwsilence.topmovies.model.Movie
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
  private var page = 1
  val listAdapter by lazy { MovieListAdapter(loadMoreMovie) }

  private val loadMoreMovie: Single<ArrayList<Movie>> = Single.create { sub ->
    viewModelScope.launch(Dispatchers.Default) {
      Log.d("TopMovies", "page: $page")
      try {
        val response = RetrofitInstance.movieApi.getPopularMovies(page).awaitResponse()
        if (response.isSuccessful) {
          val movies = response.body()?.results
          ++page
          sub.onSuccess(movies)
        } else {
          sub.onError(Exception("loadMoreMovie error"))
        }
      } catch (e: Exception) {
        e.localizedMessage?.let { Log.d("TopMovies", it) }
      }
    }
  }
}
