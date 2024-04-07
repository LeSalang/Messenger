package com.lesa.app.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lesa.app.R
import com.lesa.app.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding
        get() = _binding!! // TODO

    private var searchView: SearchView? = null
    private lateinit var queryTextListener: SearchView.OnQueryTextListener
    private var isSearch = false

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
        setUpSearchView() // TODO: It hasn't worked yet((
        setUpPager()
    }

    private fun setUpPager() {
        val tabsNames: List<Int> = listOf(R.string.channels_subscribed, R.string.channels_all_streams)
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = binding.tabLayout
        val fragmentViewPager = binding.fragmentViewPager
        binding.fragmentViewPager.adapter = pagerAdapter
        pagerAdapter.update(listOf(
            createPagerFragment(ChannelsPagerFragment(), PAGER_SUBSCRIBED),
            createPagerFragment(ChannelsPagerFragment(), PAGER_ALL)
        ))
        TabLayoutMediator(tabLayout, fragmentViewPager) { tab, position ->
            tab.text = resources.getString(tabsNames[position])
        }.attach()
    }

    private fun createPagerFragment(fragment: Fragment, arg: String) : Fragment {
        val arguments = Bundle()
        arguments.putString(PAGER_KEY, arg)
        fragment.arguments = arguments
        return fragment
    }

    private fun setUpSearchView() {
        binding.apply {
            searchIcon.setOnClickListener {
                if (isSearch) {
                    searchIcon.setImageResource(R.drawable.icon_close)
                    searchTitle.visibility = GONE
                    searchEditText.visibility = VISIBLE
                } else {
                    searchIcon.setImageResource(R.drawable.icon_search)
                    searchTitle.visibility = VISIBLE
                    searchEditText.visibility = GONE
                }
                isSearch = !isSearch
            }
        }

       /* binding.toolBar.inflateMenu(R.menu.search_menu)
        setHasOptionsMenu(true)
        val searchItem = binding.toolBar.menu.findItem(R.id.search)*/
        /*val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.i("onQueryTextChange", newText!!)
                    return true
                }
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.i("onQueryTextSubmit", query!!)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }*/
    }

    companion object {
        const val PAGER_KEY = "PagerKey"
        const val PAGER_SUBSCRIBED = "PagerSubscribedStreams"
        const val PAGER_ALL = "PagerAllStreams"
    }
}