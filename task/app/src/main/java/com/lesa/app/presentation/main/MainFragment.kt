package com.lesa.app.presentation.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.databinding.FragmentMainBinding
import com.lesa.app.presentation.navigation.Screens
import com.lesa.app.presentation.utils.BottomBarViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding()
    private lateinit var navigator: AppNavigator
    private val bottomBarViewModel by activityViewModels<BottomBarViewModel>()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.INSTANCE.appComponent.inject(this)
        navigator = AppNavigator(
            requireActivity(), R.id.mainFragmentContainer,
            childFragmentManager
        )
        viewLifecycleOwner.lifecycleScope.launch {
            bottomBarViewModel.isBottomBarShown.collect {
                showBottomBar(it)
            }
        }
        val bottomNavigationBar = binding.mainBottomNavigation
        bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.channelsScreen -> router.newRootScreen(Screens.StreamsContainer())
                R.id.peopleScreen -> router.newRootScreen(Screens.People())
                R.id.profileScreen -> router.newRootScreen(Screens.Profile())
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun showBottomBar(isVisible: Boolean) {
        binding.mainBottomNavigation.isVisible = isVisible
    }
}