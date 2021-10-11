package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.adapter.MovieListAdapter
import com.kwsilence.topmovies.data.MovieDatabase
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
  val movies: MutableLiveData<List<Movie>?> = MutableLiveData(null)
  val lossConnection: MutableLiveData<Boolean> = MutableLiveData(false)
  private var page = 1
  private val apiMovieRepository = ApiMovieRepository()
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  fun loadMoreMovie() {
    viewModelScope.launch(Dispatchers.Default) {
      try {
        Log.d("TopMovies", "page: $page")
        val dbList = roomMovieRepository.readMovieList(page)
        if (dbList.isNotEmpty()) {
          withContext(Dispatchers.Main) {
            movies.value = dbList
            ++page
          }
        } else {
          if (InternetChecker.checkInternetConnection()) {
            withContext(Dispatchers.Main) {
              lossConnection.value = false
            }
            val response = apiMovieRepository.getPopularMovies(page).awaitResponse()
            if (response.isSuccessful) {
              val res = response.body()?.results
              res?.let { list ->
                for (m in list) {
                  m.page = page
                }
                val nList = list.toList()
                withContext(Dispatchers.Main) {
                  movies.value = nList
                  ++page
                }
                roomMovieRepository.addMovies(nList)
              }
            }
          } else {
            withContext(Dispatchers.Main) {
              lossConnection.value = true
              lossConnection.value = false
            }
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
        roomMovieRepository.deleteAllMovie()
        page = 1
        movies.value = ArrayList()
      }
    } else {
      lossConnection.value = true
      lossConnection.value = false
    }
  }

  fun idle() {
    movies.value = null
  }
}
