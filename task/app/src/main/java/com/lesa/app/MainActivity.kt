package com.lesa.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var navigatorHolder: NavigatorHolder = App.INSTANCE.navigatorHolder
    private val navigator = AppNavigator(this, R.id.containerFragment)
    private val router = INSTANCE.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationBar = binding.mainBottomNavigation

        bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.channelsScreen -> router.navigateTo(Screens.Chat())
                R.id.peopleScreen -> router.navigateTo(Screens.AnotherProfile())
                R.id.profileScreen -> router.navigateTo(Screens.Profile())
            }
            true
        }

       /* val currentFragment = binding.containerFragment.children?.first()?.id
        if (currentFragment == R.id.peopleScreen) {
            binding.mainBottomNavigation.visibility = GONE
        }*/

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, ChatFragment(), ChatFragment.TAG)
                .addToBackStack(null).commit()
        }*/

    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun isBottomNavigationVisible() {

    }

}