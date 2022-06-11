package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.adapter.ScheduledListAdapter
import com.kwsilence.topmovies.db.MovieDatabase
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.notification.NotificationScheduler
import com.kwsilence.topmovies.repository.RoomMovieRepository
import com.kwsilence.topmovies.util.DateFormatter
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationFragmentViewModel(application: Application) : AndroidViewModel(application) {
  val listAdapter by lazy { ScheduledListAdapter() }
  val scheduledMovies: LiveData<List<Movie>>
  private val _scheduleResult: MutableLiveData<Result<String>> = MutableLiveData()
  val scheduleResult: LiveData<Result<String>> = _scheduleResult
  private val _deleteResult: MutableLiveData<Result<String>> = MutableLiveData()
  val deleteResult: LiveData<Result<String>> = _deleteResult
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  init {
    scheduledMovies = roomMovieRepository.readLiveSchedule()
  }

  fun schedule(movie: Movie, time: Date) {
    viewModelScope.launch(Dispatchers.IO) {
      val sTime = DateFormatter.parse(time)
      movie.schedule = sTime
      try {
        roomMovieRepository.updateMovie(movie)
        NotificationScheduler.schedule(getApplication(), time.time, movie.id, movie.title, sTime)
        _scheduleResult.postValue(Result.success("scheduled: ${movie.schedule}"))
      } catch (e: Exception) {
        Log.e("TopMovies", "schedule: ${e.localizedMessage}")
        _scheduleResult.postValue(Result.failure(e))
      }
    }
  }

  fun deleteNotification(movie: Movie) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        when (movie.page) {
          0 -> roomMovieRepository.deleteMovie(movie)
          else -> {
            movie.schedule = null
            roomMovieRepository.updateMovie(movie)
          }
        }
        NotificationScheduler.cancel(getApplication(), movie.id)
        _deleteResult.postValue(Result.success("notification deleted"))
      } catch (e: Exception) {
        Log.e("TopMovies", "submit schedule: ${e.localizedMessage}")
        _deleteResult.postValue(Result.failure(e))
      }
    }
  }
}
