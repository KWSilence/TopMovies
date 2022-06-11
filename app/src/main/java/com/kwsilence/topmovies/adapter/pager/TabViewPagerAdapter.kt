package com.kwsilence.topmovies.adapter.pager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabViewPagerAdapter(
  fm: FragmentManager,
  l: Lifecycle,
  private val fragments: List<Fragment>
) : FragmentStateAdapter(fm, l), TabLayoutMediator.TabConfigurationStrategy {
  override fun getItemCount(): Int = fragments.size

  override fun createFragment(position: Int): Fragment = fragments[position]

  override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
    tab.text = fragments[position].title
  }

  companion object {
    var Fragment.title: String?
      get() = arguments?.getString("title")
      set(value) {
        when (arguments) {
          null -> arguments = bundleOf("title" to value)
          else -> arguments?.putString("title", value)
        }
      }
  }
}
