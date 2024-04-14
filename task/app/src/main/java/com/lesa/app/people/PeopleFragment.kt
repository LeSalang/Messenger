package com.lesa.app.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.Screens
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentPeopleBinding
import com.lesa.app.model.User
import kotlinx.coroutines.launch

class PeopleFragment: Fragment() {
    private val binding: FragmentPeopleBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private lateinit var adapter: CompositeAdapter
    private val viewModel: PeopleViewModel by viewModels { UserViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModel.getAllUsers()
        lifecycleScope.launch {
            viewModel.state.collect(::render)
        }
    }

    private fun setUpRecyclerView() {
        adapter = CompositeAdapter(
            delegatesList(
                UserDelegateAdapter(onClick = { openProfile(it) })
            )
        )
        binding.peopleRecyclerView.adapter = adapter
    }

    private fun openProfile(userId: Int) {
        val users = viewModel.state.value.users
        val user = users.firstOrNull {
            it.id == userId
        }
        if (user != null) INSTANCE.router.navigateTo(Screens.AnotherProfile(user = user))
    }

    private fun render(state: PeopleScreenState) {
        when (state) {
            is PeopleScreenState.DataLoaded -> {
                binding.apply {
                    peopleRecyclerView.visibility = View.VISIBLE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.GONE
                }
                updateList(list = state.users)
            }
            PeopleScreenState.Error -> {
                binding.apply {
                    peopleRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                }
            }
            PeopleScreenState.Loading -> {
                binding.apply {
                    peopleRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateList(list: List<User>) {
        adapter.submitList(
            makeDelegateItems(
                list = list
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