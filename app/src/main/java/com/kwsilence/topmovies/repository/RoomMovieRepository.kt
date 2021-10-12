package com.kwsilence.topmovies.repository

import androidx.lifecycle.LiveData
import com.kwsilence.topmovies.data.MovieDao
import com.kwsilence.topmovies.model.Movie

class RoomMovieRepository(private val dao: MovieDao) {
  suspend fun addOrUpdateMovies(movies: List<Movie>) {
    dao.addOrUpdateMovies(movies)
  }

  suspend fun updateMovie(movie: Movie) {
    dao.updateMovie(movie)
  }

  fun readAllMovies(): LiveData<List<Movie>> = dao.readAllMovie()

  fun getLastPage(): Int? = dao.getLastPage()

  suspend fun deleteMovies() {
    dao.deleteMovies()
    dao.resetPages()
  }

  suspend fun deleteMovie(movie: Movie) {
    dao.deleteMovie(movie)
  }
}
