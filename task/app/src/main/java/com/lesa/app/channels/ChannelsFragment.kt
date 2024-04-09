package com.lesa.app.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lesa.app.R
import com.lesa.app.databinding.FragmentChannelsBinding
import kotlinx.coroutines.launch

class ChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding
        get() = _binding!! // TODO

    private val viewModel: ChannelsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchView()
        setUpPager()
        lifecycleScope.launch {
            viewModel.loadChannels()
        }

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