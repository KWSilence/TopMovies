package com.kwsilence.topmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.kwsilence.topmovies.adapter.pager.TabViewPagerAdapter
import com.kwsilence.topmovies.databinding.FragmentMainBinding
import com.kwsilence.topmovies.viewmodel.MainFragmentViewModel

class MainFragment : Fragment() {
  private var _binding: FragmentMainBinding? = null
  private val binding get() = _binding!!
  private val viewModel: MainFragmentViewModel by viewModels()
  private lateinit var adapter: TabViewPagerAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMainBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initAppViewPagerAdapter()
    binding.viewPager.adapter = adapter
    TabLayoutMediator(binding.tabLayout, binding.viewPager, adapter).attach()
  }

  private fun initAppViewPagerAdapter() {
    adapter = TabViewPagerAdapter(
      requireActivity().supportFragmentManager,
      lifecycle,
      viewModel.getFragments(requireContext())
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
