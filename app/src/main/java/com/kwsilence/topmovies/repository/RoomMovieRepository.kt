package com.kwsilence.topmovies.repository

import androidx.lifecycle.LiveData
import com.kwsilence.topmovies.db.MovieDao
import com.kwsilence.topmovies.model.Movie

class RoomMovieRepository(private val dao: MovieDao) {
  suspend fun addOrUpdateMovies(movies: List<Movie>) {
    dao.addOrUpdateMovies(movies)
  }

  suspend fun addMovie(movie: Movie) {
    dao.addMovie(movie)
  }

  suspend fun updateMovie(movie: Movie) {
    dao.updateMovie(movie)
  }

  fun readLivePages(page: Int): LiveData<List<Movie>> = dao.readLivePages(page)

  fun readLiveSchedule(): LiveData<List<Movie>> = dao.readLiveSchedule()

  fun readAllMovies(): List<Movie> = dao.readAllMovies()

  fun getLastPage(): Int? = dao.getLastPage()

  fun getMovie(id: Int): Movie? = dao.getMovie(id)

  suspend fun deleteMovies() {
    dao.deleteMovies()
  }

  suspend fun deleteAll() {
    dao.deleteAll()
  }

  suspend fun deleteMovie(movie: Movie) {
    dao.deleteMovie(movie)
  }
}
