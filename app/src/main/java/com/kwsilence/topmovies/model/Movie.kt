package com.kwsilence.topmovies.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie_table")
data class Movie(
  @PrimaryKey(autoGenerate = false)
  val id: Int,
  @SerializedName("original_language")
  val originalLang: String,
  @SerializedName("original_title")
  val originalTitle: String,
  val overview: String,
  val popularity: Double,
  @SerializedName("poster_path")
  val posterPath: String?,
  @SerializedName("release_date")
  val releaseDate: String?,
  val title: String,
  @SerializedName("vote_average")
  val voteAverage: Double,
  @SerializedName("vote_count")
  val voteCount: Int,

  var page: Int,
  var schedule: String?
) : Parcelable
