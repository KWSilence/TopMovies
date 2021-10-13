package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.adapter.ScheduledListAdapter
import com.kwsilence.topmovies.db.MovieDatabase
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.notification.NotificationScheduler
import com.kwsilence.topmovies.repository.RoomMovieRepository
import com.kwsilence.topmovies.util.DateFormatter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationFragmentViewModel(application: Application) : AndroidViewModel(application) {
  val listAdapter by lazy { ScheduledListAdapter() }
  val scheduledMovies: LiveData<List<Movie>>
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  init {
    scheduledMovies = roomMovieRepository.readLiveSchedule()
  }

  fun schedule(movie: Movie, time: Date): Single<Movie> = Single.create { sub ->
    viewModelScope.launch(Dispatchers.IO) {
      val sTime = DateFormatter.parse(time)
      movie.schedule = sTime
      try {
        roomMovieRepository.updateMovie(movie)
      } catch (e: Exception) {
        Log.e("TopMovies", "schedule: ${e.localizedMessage}")
      }
      NotificationScheduler.schedule(getApplication(), time.time, movie.id, movie.title, sTime)
      sub.onSuccess(movie)
    }
  }

  fun deleteNotification(movie: Movie): Completable = Completable.create { sub ->
    viewModelScope.launch(Dispatchers.IO) {
      try {
        if (movie.page == 0) {
          roomMovieRepository.deleteMovie(movie)
        } else {
          movie.schedule = null
          roomMovieRepository.updateMovie(movie)
        }
        NotificationScheduler.cancel(getApplication(), movie.id)
      } catch (e: Exception) {
        sub.onError(e)
      }
      sub.onComplete()
    }
  }
}
