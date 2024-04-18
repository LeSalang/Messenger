package com.lesa.app.presenation.channels

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.lesa.app.R
import com.lesa.app.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    private val binding: FragmentChannelsBinding by viewBinding()
    private val viewModel: ChannelsViewModel by viewModels { ChannelsViewModelFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchView()
        setUpPager()
        viewModel.loadChannels()

        binding.searchEditText.addTextChangedListener {
            it?.let {
                viewModel.searchQuery.tryEmit(it.toString())
            }
        }
    }

    private fun setUpPager() {
        val pages = listOf(ChannelsScreenType.SUBSCRIBED, ChannelsScreenType.ALL)
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = binding.tabLayout
        val fragmentViewPager = binding.fragmentViewPager
        binding.fragmentViewPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                ChannelsPagerFragment(),
                ChannelsPagerFragment()
            )
        )
        TabLayoutMediator(tabLayout, fragmentViewPager) { tab, position ->
            tab.text = resources.getString(pages[position].title)
        }.attach()
        binding.fragmentViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setUpScreenType(pages[position])
            }
        })
    }

    private fun setUpSearchView() {
        binding.apply {
            searchIcon.setOnClickListener {
                if (viewModel.isTitleSearch) {
                    searchIcon.setImageResource(R.drawable.icon_close)
                    searchTitle.visibility = GONE
                    searchEditText.visibility = VISIBLE
                } else {
                    searchIcon.setImageResource(R.drawable.icon_search)
                    searchTitle.visibility = VISIBLE
                    searchEditText.visibility = GONE
                    searchEditText.text.clear()
                }
                viewModel.isTitleSearch = !viewModel.isTitleSearch
            }
        }
    }
}