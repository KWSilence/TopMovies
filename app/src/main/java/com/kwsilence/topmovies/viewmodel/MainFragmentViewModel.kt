package com.kwsilence.topmovies.viewmodel

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.adapter.pager.TabViewPagerAdapter.Companion.title
import com.kwsilence.topmovies.fragment.MovieListFragment
import com.kwsilence.topmovies.fragment.ScheduledListFragment

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
  private var fragments: List<Fragment>? = null

  fun getFragments(context: Context): List<Fragment> =
    when (val list = fragments) {
      null -> {
        listOf(
          MovieListFragment().apply { title = context.getString(R.string.tab_movie_list) },
          ScheduledListFragment().apply { title = context.getString(R.string.tab_scheduled_list) }
        ).also { fragments = it }
      }
      else -> list
    }
}
