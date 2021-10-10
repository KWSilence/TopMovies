package com.kwsilence.topmovies.state

sealed class MovieListState {
  object Default : MovieListState()
  object LoadMore : MovieListState()
  object Refresh : MovieListState()
}
