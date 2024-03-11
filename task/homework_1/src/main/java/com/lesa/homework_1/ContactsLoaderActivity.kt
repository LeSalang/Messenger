package com.lesa.homework_1

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lesa.homework_1.ContactService.Companion.ACTION_CONTACTS_RECEIVED
import com.lesa.homework_1.databinding.ActivityContactsLoaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsLoaderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsLoaderBinding
    private var service: ContactService? = null
    private val coroutine = CoroutineScope(Dispatchers.IO)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_CONTACTS_RECEIVED) {
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.LocalBinder
            this@ContactsLoaderActivity.service = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, ContactService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsLoaderBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonGetContactList.setOnClickListener {
            onGetContactListButtonClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(receiver, IntentFilter(ACTION_CONTACTS_RECEIVED))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            readContacts()
        }
    }

    private fun onGetContactListButtonClicked() {
        val permission = Manifest.permission.READ_CONTACTS
        val permissionStatus = ActivityCompat.checkSelfPermission(this, permission)
        val permissionGranted = permissionStatus == PackageManager.PERMISSION_GRANTED
        if (permissionGranted) {
            readContacts()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
        }
    }

    private fun readContacts() {
        coroutine.launch {
            service?.loadContactList()
        }
    }

    companion object {
        private const val REQUEST_CODE = 0
    }
}