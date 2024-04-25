package com.lesa.app.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.R
import com.lesa.app.databinding.ActivityMainBinding
import com.lesa.app.presentation.navigation.Screens

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var navigatorHolder: NavigatorHolder = INSTANCE.navigatorHolder
    private val navigator = AppNavigator(this, R.id.containerFragment)
    private val router = INSTANCE.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) router.newRootScreen(Screens.Main())
        window?.statusBarColor = resources.getColor(R.color.gray_18)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}