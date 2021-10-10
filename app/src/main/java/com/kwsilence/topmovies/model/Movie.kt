package com.kwsilence.topmovies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
  var id: Int,
  var backdrop_path: String?,
  var overview: String,
  var popularity: Double,
  @SerializedName("poster_path")
  var posterPath: String?,
  @SerializedName("release_date")
  var releaseDate: String?,
  var title: String,
  @SerializedName("vote_average")
  var voteAverage: Double,
) : Parcelable
