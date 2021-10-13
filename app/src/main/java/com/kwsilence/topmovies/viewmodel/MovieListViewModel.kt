package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
  private val mutablePage: MutableLiveData<Int> = MutableLiveData(0)
  private val apiMovieRepository = ApiMovieRepository()
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  init {
    movies = Transformations.switchMap(mutablePage) { p -> roomMovieRepository.readLivePages(p) }
  }

  fun loadMoreMovie() {
    val mPage = mutablePage.value?.plus(1)
    mPage ?: return
    viewModelScope.launch(Dispatchers.Default) {
      try {
        Log.d("TopMovies", "page: $mPage")
        val lPage = roomMovieRepository.getLastPage()
        if (lPage != null && lPage >= mPage) {
          Log.d("TopMovies", "loadFrom: DB")
          setPage(mPage)
        } else {
          if (InternetChecker.checkInternetConnection()) {
            Log.d("TopMovies", "loadFrom: API")
            val response = apiMovieRepository.getPopularMovies(mPage).awaitResponse()
            if (response.isSuccessful) {
              response.body()?.results?.let { res ->
                for (m in res) {
                  m.page = mPage
                }
                roomMovieRepository.addOrUpdateMovies(res.toList())
                setPage(mPage)
              }
            }
          } else {
            lossConnect(true)
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
        setPage(0)
      }
    } else {
      lossConnection.value = true
    }
  }

  fun messageShown() {
    lossConnection.value = false
  }

  private suspend fun setPage(page: Int) {
    withContext(Dispatchers.Main) {
      mutablePage.value = page
    }
  }

  private suspend fun lossConnect(isLoss: Boolean) {
    withContext(Dispatchers.Main) {
      lossConnection.value = isLoss
    }
  }
}
