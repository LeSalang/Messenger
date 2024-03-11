package com.lesa.homework_1

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ContactService : Service() {
    inner class LocalBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    private val binder = LocalBinder()

    private var contactColumns = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun loadContactList() {
        val list = arrayListOf<Contact>()
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            contactColumns,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        ).use {
            it?.apply {
                val nameIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (moveToNext()) {
                    val name: String = getString(nameIndex)
                    val number: String = getString(numberIndex)
                    list.add(Contact(name = name, number = number))
                }
            }
        }
        LocalBroadcastManager
            .getInstance(this)
            .sendBroadcast(Intent(ACTION_CONTACTS_RECEIVED).putExtra(CONTACT_LIST_DATA_NAME, list))
    }

    companion object {
        const val ACTION_CONTACTS_RECEIVED = "com.lesa.homework_1.ACTION_CONTACTS_RECEIVED"
        const val CONTACT_LIST_DATA_NAME = "contacts"
    }
}