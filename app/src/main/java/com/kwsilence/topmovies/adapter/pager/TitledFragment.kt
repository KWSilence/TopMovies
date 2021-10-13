package com.kwsilence.topmovies.adapter.pager

import androidx.fragment.app.Fragment

abstract class TitledFragment(private val title: String) : Fragment() {
  fun getTitle(): String = title
}
