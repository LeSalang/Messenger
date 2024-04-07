package com.lesa.app.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.R
import com.lesa.app.Screens
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.databinding.FragmentPeopleBinding
import com.lesa.app.model.User
import com.lesa.app.stubPeople

class PeopleFragment: Fragment() {
    private var _binding: FragmentPeopleBinding? = null
    private val binding
        get() = _binding!! // TODO

    private lateinit var adapter: CompositeAdapter

    private var searchView: SearchView? = null
    private lateinit var queryTextListener: SearchView.OnQueryTextListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleView()
        setUpSearchView() //TODO
    }

    private fun setUpSearchView() {
        binding.toolBar.inflateMenu(R.menu.search_menu)
        setHasOptionsMenu(true)
        val searchItem = binding.toolBar.menu.findItem(R.id.search)
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

    private fun setUpRecycleView() {
        val delegates: List<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>> = listOf(
            UserDelegateAdapter(onClick = { openProfile(it) }) as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>,
        )
        adapter = CompositeAdapter(delegates)
        binding.peopleRecyclerView.adapter = adapter
        updateList()
    }

    private fun openProfile(userId: Int) {
        INSTANCE.router.navigateTo(Screens.AnotherProfile(userId = userId))
    }

    private fun updateList() {
        adapter.submitList(
            makeDelegateItems(
                list = stubPeople
            )
        )
    }

    private fun makeDelegateItems(
        list: List<User>,
    ): List<DelegateItem> {
        return list.map {
            UserDelegateItem(it)
        }
    }
}