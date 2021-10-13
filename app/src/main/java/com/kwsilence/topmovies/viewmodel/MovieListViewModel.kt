package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.db.MovieDatabase
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.repository.ApiMovieRepository
import com.kwsilence.topmovies.repository.RoomMovieRepository
import com.kwsilence.topmovies.util.InternetChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
  val listAdapter by lazy { MovieListAdapter() }

  val movies: LiveData<List<Movie>>
  val lossConnection: MutableLiveData<Boolean> = MutableLiveData(false)
  private var page = 1
  private val apiMovieRepository = ApiMovieRepository()
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  init {
    movies = roomMovieRepository.readLiveMovies()
  }

  fun loadMoreMovie() {
    viewModelScope.launch(Dispatchers.Default) {
      try {
        Log.d("TopMovies", "page: $page")
        val lPage = roomMovieRepository.getLastPage()
        if (lPage != null && lPage > page) {
          page = lPage
        }
        if (InternetChecker.checkInternetConnection()) {
          val response = apiMovieRepository.getPopularMovies(page).awaitResponse()
          if (response.isSuccessful) {
            val res = response.body()?.results
            res?.let { list ->
              for (m in list) {
                m.page = page
              }
              val nList = list.toList()
              ++page
              roomMovieRepository.addOrUpdateMovies(nList)
            }
          }
        } else {
          withContext(Dispatchers.Main) {
            lossConnection.value = true
          }
        }
      } catch (e: Exception) {
        Log.d("TopMovies", "loadMoreMovie: ${e.localizedMessage}")
      }
    }
  }

  fun refreshMovie() {
    if (InternetChecker.checkInternetConnection()) {
      viewModelScope.launch {
        roomMovieRepository.deleteMovies()
        page = 1
      }
    } else {
      lossConnection.value = true
    }
  }

  fun messageShown() {
    lossConnection.value = false
  }
}
