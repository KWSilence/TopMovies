package com.kwsilence.topmovies.repository

import com.kwsilence.topmovies.api.RetrofitInstance
import com.kwsilence.topmovies.model.TopMoviesPage
import retrofit2.Call

class ApiMovieRepository {
  fun getPopularMovies(page: Int): Call<TopMoviesPage> =
    RetrofitInstance.movieApi.getPopularMovies(page)
}
