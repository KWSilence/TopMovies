package com.kwsilence.topmovies.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kwsilence.topmovies.model.Movie
import com.kwsilence.topmovies.repository.RoomMovieRepository
import java.io.IOException
import kotlin.jvm.Throws
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseUnitTest {
  private lateinit var db: MovieDatabase
  private lateinit var dao: MovieDao
  private lateinit var repository: RoomMovieRepository

  @Before
  fun createDb() {
    val context: Context = ApplicationProvider.getApplicationContext()
    db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.movieDao()
    repository = RoomMovieRepository(dao)
  }

  @Test
  fun testAdd() = runBlocking {
    val input = Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 1, null)
    repository.addMovie(input)
    val result2 = repository.getMovie(input.id)
    Assert.assertEquals(input, result2)
  }

  @Test
  fun testUpdate() = runBlocking {
    val input = Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 1, null)
    repository.addMovie(input)
    val result = repository.getMovie(input.id)
    Assert.assertEquals(input, result)

    val changedInput = Movie(input.id, "2", "2", "2", 2.0, null, null, "2", 2.0, 2, 2, null)
    repository.updateMovie(changedInput)
    val result2 = repository.getMovie(input.id)
    Assert.assertEquals(changedInput, result2)
  }

  @Test
  fun testAddOrUpdate() = runBlocking {
    val input = listOf(Movie(2, "2", "2", "2", 3.0, null, null, "2", 2.0, 2, 2, null))
    repository.addOrUpdateMovies(input)
    val result = repository.readAllMovies()
    Assert.assertEquals(result.size, 1)
    Assert.assertEquals(input, result)

    val input2 = listOf(
      Movie(input.first().id, "4", "7", "8", 2.0, null, null, "1", 1.0, 1, 1, null),
      Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 1, null)
    )
    repository.addOrUpdateMovies(input2)
    val result2 = repository.readAllMovies()
    Assert.assertEquals(result2.size, 2)
    Assert.assertEquals(input2, result2)
  }

  @Test
  fun testGetLastPage() = runBlocking {
    val result = repository.getLastPage()
    Assert.assertNull(result)

    val input = listOf(
      Movie(2, "4", "7", "8", 2.0, null, null, "1", 1.0, 1, 1, null),
      Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 7, null),
      Movie(3, "1", "1", "1", 4.0, null, null, "1", 1.0, 1, 4, null)
    )
    repository.addOrUpdateMovies(input)
    val result2 = repository.getLastPage()
    Assert.assertEquals(input.maxByOrNull { m -> m.page }?.page, result2)
  }

  @Test
  fun testDelete() = runBlocking {
    val input = listOf(
      Movie(2, "4", "7", "8", 4.0, null, null, "1", 1.0, 1, 1, null),
      Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 7, "some date"),
      Movie(3, "1", "1", "1", 2.0, null, null, "1", 1.0, 1, 4, null)
    )
    repository.addOrUpdateMovies(input)
    repository.deleteMovie(input[1])
    val result = repository.readAllMovies()
    Assert.assertEquals(result.size, 2)
    Assert.assertEquals(result, listOf(input[0], input[2]))

    repository.deleteAll()
    val result2 = repository.readAllMovies()
    Assert.assertTrue(result2.isEmpty())
  }

  @Test
  fun testDeleteNonScheduledMovies() = runBlocking {
    val input = listOf(
      Movie(2, "4", "7", "8", 2.0, null, null, "1", 1.0, 1, 1, null),
      Movie(1, "1", "1", "1", 1.0, null, null, "1", 1.0, 1, 7, "some date"),
      Movie(3, "1", "1", "1", 4.0, null, null, "1", 1.0, 1, 4, null)
    )
    repository.addOrUpdateMovies(input)
    repository.deleteMovies()
    val result = repository.readAllMovies()
    Assert.assertEquals(result.size, 1)
    Assert.assertEquals(result.first().id, input[1].id)
    Assert.assertEquals(result.first().page, 0)
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db.close()
  }
}
