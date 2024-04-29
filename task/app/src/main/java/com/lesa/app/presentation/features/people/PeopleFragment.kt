package com.lesa.app.presentation.features.people

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.lesa.app.R
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentPeopleBinding
import com.lesa.app.di.people.PeopleComponent
import com.lesa.app.di.people.PeopleComponentViewModel
import com.lesa.app.presentation.elm.ElmBaseFragment
import com.lesa.app.presentation.features.people.elm.PeopleEvent
import com.lesa.app.presentation.features.people.elm.PeopleState
import com.lesa.app.presentation.features.people.elm.PeopleStoreFactory
import com.lesa.app.presentation.features.people.model.UserUi
import com.lesa.app.presentation.main.MainFragment
import com.lesa.app.presentation.navigation.Screens
import com.lesa.app.presentation.utils.ScreenState
import com.lesa.app.presentation.utils.hideKeyboard
import com.lesa.app.presentation.utils.showKeyboard
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.lesa.app.presentation.features.people.elm.PeopleEffect as Effect
import com.lesa.app.presentation.features.people.elm.PeopleEvent as Event
import com.lesa.app.presentation.features.people.elm.PeopleState as State

class PeopleFragment: ElmBaseFragment<Effect, State, Event>(
    R.layout.fragment_people
) {
    private val binding: FragmentPeopleBinding by viewBinding()
    private lateinit var adapter: CompositeAdapter

    @Inject
    lateinit var storeFactory: PeopleStoreFactory

    @Inject
    lateinit var router: Router

    lateinit var peopleComponent: PeopleComponent

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        storeFactory.create()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<PeopleComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.accept(PeopleEvent.Ui.Init)
        setUpViews()
    }

    override fun render(state: PeopleState) {
        when (val dataToRender = state.peopleUi) {
            is ScreenState.Content -> {
                val content = dataToRender.content
                binding.apply {
                    peopleRecyclerView.visibility = View.VISIBLE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.GONE
                }
                updateList(list = content)
            }
            ScreenState.Error -> {
                binding.apply {
                    peopleRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                }
            }
            ScreenState.Loading -> {
                binding.apply {
                    peopleRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.VISIBLE
                }
            }
        }
        if (state.isSearching) {
            binding.apply {
                searchIcon.setImageResource(R.drawable.icon_close)
                searchTitle.visibility = View.GONE
                searchEditText.visibility = View.VISIBLE
                searchEditText.showKeyboard()
            }
        } else {
            binding.apply {
                searchIcon.setImageResource(R.drawable.icon_search)
                searchTitle.visibility = View.VISIBLE
                searchEditText.visibility = View.GONE
                searchEditText.text.clear()
                searchEditText.hideKeyboard()
            }
        }
        val fragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.containerFragment) as? MainFragment
        fragment?.showBottomBar(!state.isSearching)
    }

    private fun setUpViews() {
        setUpRecyclerView()
        setupRefreshButton()
        setSearchListener()
    }

    private fun setUpRecyclerView() {
        adapter = CompositeAdapter(
            delegatesList(
                UserDelegateAdapter(onClick = { openProfile(it) })
            )
        )
        binding.peopleRecyclerView.adapter = adapter
    }

    private fun setupRefreshButton() {
        binding.error.refreshButton.setOnClickListener {
            store.accept(PeopleEvent.Ui.ReloadPeople)
        }
    }

    private fun setSearchListener() {
        binding.searchIcon.setOnClickListener {
            store.accept(PeopleEvent.Ui.OnSearchClicked)
        }
        binding.searchEditText.addTextChangedListener {
            store.accept(PeopleEvent.Ui.Search(it.toString()))
        }
    }

    private fun updateList(list: List<UserUi>) {
        adapter.submitList(
            makeDelegateItems(
                list = list
            )
        )
    }

    private fun makeDelegateItems(
        list: List<UserUi>,
    ): List<DelegateItem> {
        return list.map {
            UserDelegateItem(it)
        }
    }

    private fun openProfile(user: UserUi) {
        router.navigateTo(Screens.AnotherProfile(user = user))
    }
}