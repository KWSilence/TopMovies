package com.kwsilence.topmovies.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
  private const val baseApiURL = "https://api.themoviedb.org/3/"
  const val apiKey = "e0621399003b72fad276dc4972cbed5f"

  private val retrofitMovie by lazy {
    Retrofit.Builder()
      .baseUrl(baseApiURL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  val movieApi: MovieApi by lazy {
    retrofitMovie.create(MovieApi::class.java)
  }
}
