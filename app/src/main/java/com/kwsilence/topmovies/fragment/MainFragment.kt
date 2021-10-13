package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.kwsilence.topmovies.R
import com.kwsilence.topmovies.adapter.pager.TabViewPagerAdapter
import com.kwsilence.topmovies.adapter.pager.TitledFragment
import com.kwsilence.topmovies.databinding.FragmentMainBinding
import com.kwsilence.topmovies.viewmodel.MainFragmentViewModel

class MainFragment : Fragment() {
  private lateinit var binding: FragmentMainBinding
  private val viewModel: MainFragmentViewModel by viewModels()
  private lateinit var adapter: TabViewPagerAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMainBinding.inflate(inflater, container, false)
    initAppViewPagerAdapter()
    binding.viewPager.adapter = adapter
    TabLayoutMediator(binding.tabLayout, binding.viewPager, adapter).attach()
    return binding.root
  }

  private fun initAppViewPagerAdapter() {
    if (!viewModel.isFilled()) {
      val list = ArrayList<TitledFragment>()
      list.add(MovieListFragment(getString(R.string.tab_movie_list)))
      list.add(ScheduledListFragment(getString(R.string.tab_scheduled_list)))
      viewModel.fillFragments(list)
    }

    adapter = TabViewPagerAdapter(
      requireActivity().supportFragmentManager,
      lifecycle,
      viewModel.fragments
    )
  }
}
