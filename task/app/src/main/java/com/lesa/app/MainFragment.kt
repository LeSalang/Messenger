package com.lesa.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private val binding: FragmentMainBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val cicerone = Cicerone.create()
    private val router = cicerone.router
    private val navigatorHolder = cicerone.getNavigatorHolder()
    private lateinit var navigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator =
            AppNavigator(requireActivity(), R.id.mainFragmentContainer, childFragmentManager)

        val bottomNavigationBar = binding.mainBottomNavigation
        bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.channelsScreen -> router.newRootScreen(Screens.Channels())
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
}