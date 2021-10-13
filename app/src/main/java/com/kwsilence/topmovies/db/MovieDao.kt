package com.kwsilence.topmovies.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kwsilence.topmovies.model.Movie

@Dao
interface MovieDao {
  @Insert
  suspend fun addMovie(movie: Movie)

  @Update
  suspend fun updateMovie(movie: Movie)

  @Query("select max(page) from movie_table")
  fun getLastPage(): Int?

  @Transaction
  suspend fun addOrUpdateMovies(movies: List<Movie>) {
    for (movie in movies) {
      val sMovie = getMovie(movie.id)
      if (sMovie == null) {
        addMovie(movie)
      } else {
        movie.schedule = sMovie.schedule
        updateMovie(movie)
      }
    }
  }

  @Query("select * from movie_table where page > 0 and page <= :page order by popularity desc")
  fun readLivePages(page: Int): LiveData<List<Movie>>

  @Query("select * from movie_table where schedule is not null order by schedule")
  fun readLiveSchedule(): LiveData<List<Movie>>

  @Query("select * from movie_table order by popularity desc")
  fun readAllMovies(): List<Movie>

  @Query("select * from movie_table where id = :id")
  fun getMovie(id: Int): Movie?

  @Query("delete from movie_table where schedule is null")
  suspend fun deleteNonScheduledMovies()

  @Query("update movie_table set page = 0")
  suspend fun resetPages()

  @Query("delete from movie_table")
  suspend fun deleteAll()

  @Transaction
  suspend fun deleteMovies() {
    deleteNonScheduledMovies()
    resetPages()
  }

  @Delete
  suspend fun deleteMovie(movie: Movie)
}
