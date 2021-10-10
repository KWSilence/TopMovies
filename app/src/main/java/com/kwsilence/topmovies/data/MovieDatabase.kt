package com.kwsilence.topmovies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kwsilence.topmovies.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
  abstract fun movieDao(): MovieDao

  companion object {
    @Volatile
    private var instance: MovieDatabase? = null

    fun getDatabase(context: Context): MovieDatabase {
      val tmpInstance = instance
      tmpInstance?.let { return it }
      synchronized(this) {
        val newInstance = Room.databaseBuilder(
          context.applicationContext,
          MovieDatabase::class.java,
          "movie_database"
        ).build()
        instance = newInstance
        return newInstance
      }
    }
  }
}
