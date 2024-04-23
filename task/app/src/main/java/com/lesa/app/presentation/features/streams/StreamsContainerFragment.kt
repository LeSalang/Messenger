package com.lesa.app.presentation.features.streams

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.lesa.app.R
import com.lesa.app.databinding.FragmentStreamsContainerBinding
import com.lesa.app.presentation.channels.PagerAdapter
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.main.MainFragment
import com.lesa.app.presentation.utils.hideKeyboard
import com.lesa.app.presentation.utils.showKeyboard

class StreamsContainerFragment : Fragment(R.layout.fragment_streams_container) {
    private val binding: FragmentStreamsContainerBinding by viewBinding()
    private var searchVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchView()
        setUpPager()
    }

    private fun setUpPager() {
        val pages = listOf(StreamType.SUBSCRIBED, StreamType.ALL)
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = binding.tabLayout
        val fragmentViewPager = binding.fragmentViewPager
        binding.fragmentViewPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                StreamsFragment.getNewInstance(pages[0]),
                StreamsFragment.getNewInstance(pages[1])
            )
        )
        TabLayoutMediator(tabLayout, fragmentViewPager) { tab, position ->
            tab.text = resources.getString(pages[position].title)
        }.attach()
    }

    private fun setUpSearchView() {
        binding.apply {
            searchIcon.setOnClickListener {
                if (searchVisible) {
                    searchIcon.setImageResource(R.drawable.icon_search)
                    searchTitle.visibility = VISIBLE
                    searchEditText.visibility = GONE
                    searchEditText.text.clear()
                    searchEditText.hideKeyboard()
                } else {
                    searchIcon.setImageResource(R.drawable.icon_close)
                    searchTitle.visibility = GONE
                    searchEditText.visibility = VISIBLE
                    searchEditText.showKeyboard()
                }
                val fragment = requireActivity().supportFragmentManager
                    .findFragmentById(R.id.containerFragment) as? MainFragment
                fragment?.showBottomBar(searchVisible)
                searchVisible = searchVisible.not()
            }
        }
    }

    fun setSearchListener(search: (String) -> Unit) {
        binding.searchEditText.addTextChangedListener {
            it?.let {
                search(it.toString())
            }
        }
    }
}