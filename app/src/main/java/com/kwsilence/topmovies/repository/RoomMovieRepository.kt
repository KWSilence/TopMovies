package com.kwsilence.topmovies.repository

import androidx.lifecycle.LiveData
import com.kwsilence.topmovies.data.MovieDao
import com.kwsilence.topmovies.model.Movie

class RoomMovieRepository(private val dao: MovieDao) {
  suspend fun addMovies(movies: List<Movie>) {
    dao.addMovies(movies)
  }

  fun readAllMovie(): LiveData<List<Movie>> = dao.readAllMovie()

  fun readMovieList(page: Int): List<Movie> = dao.readMovieList(page)

  suspend fun deleteAllMovie() {
    dao.deleteAllMovies()
  }
}
