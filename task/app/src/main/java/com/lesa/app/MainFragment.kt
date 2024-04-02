package com.lesa.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val cicerone = Cicerone.create()
    private val router = cicerone.router
    private val navigatorHolder = cicerone.getNavigatorHolder()
    private lateinit var navigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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