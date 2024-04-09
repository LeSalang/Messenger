package com.lesa.app.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.Screens
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentPeopleBinding
import com.lesa.app.model.User
import com.lesa.app.stubPeople

class PeopleFragment: Fragment() {
    private var _binding: FragmentPeopleBinding? = null
    private val binding
        get() = _binding!! // TODO

    private lateinit var adapter: CompositeAdapter

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
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                UserDelegateAdapter(onClick = { openProfile(it) })
            )
        )
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