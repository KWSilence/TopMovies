package com.kwsilence.topmovies.api

import com.kwsilence.topmovies.api.RetrofitInstance.apiKey
import com.kwsilence.topmovies.model.TopMoviesPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
  @GET("movie/popular")
  fun getPopularMovies(
    @Query("page") page: Int,
    @Query("api_key") api_key: String = apiKey
  ): Call<TopMoviesPage>
}
