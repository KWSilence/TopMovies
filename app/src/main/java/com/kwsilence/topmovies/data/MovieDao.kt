package com.kwsilence.topmovies.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kwsilence.topmovies.model.Movie

@Dao
interface MovieDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addMovies(movies: List<Movie>)

  @Query("select * from movie_table order by popularity desc")
  fun readAllMovie(): LiveData<List<Movie>>

  @Query("select * from movie_table where page = :page order by popularity desc")
  fun readMovieList(page: Int): List<Movie>

  @Query("delete from movie_table")
  suspend fun deleteAllMovies()
}
