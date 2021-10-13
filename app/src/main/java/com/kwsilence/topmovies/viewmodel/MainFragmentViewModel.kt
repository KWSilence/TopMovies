package com.kwsilence.topmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kwsilence.topmovies.adapter.pager.TitledFragment

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
  val fragments = ArrayList<TitledFragment>()
  private var isFilled = false

  fun fillFragments(list: List<TitledFragment>) {
    fragments.addAll(list)
    isFilled = true
  }

  fun isFilled(): Boolean = isFilled
}
