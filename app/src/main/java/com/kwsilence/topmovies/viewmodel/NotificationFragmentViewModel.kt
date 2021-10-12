package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kwsilence.topmovies.data.MovieDatabase
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.notification.NotificationScheduler
import com.kwsilence.topmovies.repository.RoomMovieRepository
import com.kwsilence.topmovies.util.DateFormatter
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationFragmentViewModel(application: Application) : AndroidViewModel(application) {
  private val roomMovieRepository =
    RoomMovieRepository(MovieDatabase.getDatabase(application.applicationContext).movieDao())

  fun schedule(movie: Movie, time: Date): Single<Movie> {
    return Single.create { sub ->
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
  }
}
