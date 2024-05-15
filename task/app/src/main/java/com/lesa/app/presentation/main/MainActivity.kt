package com.lesa.app.presentation.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.databinding.ActivityMainBinding
import com.lesa.app.presentation.navigation.Screens
import com.lesa.app.presentation.utils.NetworkReceiver
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navigator = AppNavigator(this, R.id.containerFragment)
    private var networkReceiver: NetworkReceiver? = null

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        App.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) router.newRootScreen(Screens.Main())
        window?.statusBarColor = resources.getColor(R.color.gray_18)
        networkReceiver = makeNetworkReceiver()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        networkReceiver?.let { unregisterReceiver(it) }
    }

    private fun makeNetworkReceiver(): NetworkReceiver {
        val networkReceiverListener = object : NetworkReceiver.NetworkReceiverListener {
            override fun onNetworkConnectionChanged(isConnected: Boolean) {
                toggleNoInternetBar(!isConnected)
            }
        }
        return NetworkReceiver(networkReceiverListener)
    }

    private fun toggleNoInternetBar(display: Boolean) {
        val error = binding.internetError
        if (display) {
            val enterAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom)
            error.root.startAnimation(enterAnim)
        } else {
            val exitAnim = AnimationUtils.loadAnimation(this, R.anim.exit_to_bottom)
            error.root.startAnimation(exitAnim)
        }
        error.root.visibility = if (display) View.VISIBLE else View.GONE
    }
}