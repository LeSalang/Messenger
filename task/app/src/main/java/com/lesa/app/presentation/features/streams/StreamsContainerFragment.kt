package com.lesa.app.presentation.features.streams

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.lesa.app.R
import com.lesa.app.databinding.FragmentStreamsContainerBinding
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.utils.BottomBarViewModel
import com.lesa.app.presentation.utils.PagerAdapter
import com.lesa.app.presentation.utils.hideKeyboard
import com.lesa.app.presentation.utils.showKeyboard
import kotlinx.coroutines.launch

class StreamsContainerFragment : Fragment(R.layout.fragment_streams_container) {
    private val binding: FragmentStreamsContainerBinding by viewBinding()
    private var searchVisible = false
    private lateinit var bottomBarViewModel: BottomBarViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBarViewModel = ViewModelProvider(requireActivity())[BottomBarViewModel::class.java]
        setUpViews()
    }

    private fun setUpViews() {
        setAddStreamListener()
        setUpPager()
        setUpSearchView()
    }

    private fun setAddStreamListener() {
        binding.addIcon.setOnClickListener {
            CreateStreamDialogFragment().show(
                childFragmentManager, CreateStreamDialogFragment.TAG
            )
        }
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
                viewLifecycleOwner.lifecycleScope.launch {
                    bottomBarViewModel.isBottomBarShown.emit(searchVisible)
                }
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