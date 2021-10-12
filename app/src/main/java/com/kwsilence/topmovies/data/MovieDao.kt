package com.kwsilence.topmovies.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
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

//  @Query("select * from movie_table where page = :page order by popularity desc")
//  fun readMovieList(page: Int): List<Movie>

  @Transaction
  suspend fun addOrUpdateMovies(movies: List<Movie>) {
    for (movie in movies) {
      val sMovie = getMovie(movie.id)
      if (sMovie.isEmpty()) {
        addMovie(movie)
      } else {
        movie.schedule = sMovie[0].schedule
        updateMovie(movie)
      }
    }
  }

  @Query("select * from movie_table where page > 0 order by popularity desc")
  fun readAllMovie(): LiveData<List<Movie>>

  @Query("select * from movie_table where id = :id")
  fun getMovie(id: Int): List<Movie>

  @Query("delete from movie_table where schedule is null")
  suspend fun deleteMovies()

  @Query("update movie_table set page = 0 where schedule is not null")
  suspend fun resetPages()
}
