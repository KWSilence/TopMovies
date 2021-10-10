package com.kwsilence.topmovies.model

data class TopMoviesPage(
  var page: Int,
  var results: ArrayList<Movie>,
  var total_pages: Int,
  var total_results: Int
)
