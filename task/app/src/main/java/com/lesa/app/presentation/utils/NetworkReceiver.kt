package com.lesa.app.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkReceiver(
    private val networkReceiverListener: NetworkReceiverListener
) : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent) {
        networkReceiverListener.onNetworkConnectionChanged(hasInternet(context))
    }

    private fun hasInternet(context: Context): Boolean {
        val connectMgr = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetworkInfo = connectMgr.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    interface NetworkReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}