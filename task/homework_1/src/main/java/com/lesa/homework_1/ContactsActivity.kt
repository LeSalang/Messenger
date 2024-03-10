package com.lesa.homework_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lesa.homework_1.ContactService.Companion.CONTACT_LIST_DATA_NAME
import com.lesa.homework_1.databinding.ActivityContactsBinding
import java.util.ArrayList

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding

    private val contactsLoaderActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it?.resultCode == Activity.RESULT_OK) {
            val data: ArrayList<Contact>? = it.data?.getParcelableArrayListExtra(
                CONTACT_LIST_DATA_NAME
            )
            updateContactList(list = data ?: arrayListOf())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonOpenSecondActivity.setOnClickListener {
            val intent = Intent(this, ContactsLoaderActivity::class.java)
            contactsLoaderActivityResultLauncher.launch(intent)
        }
    }

    private fun updateContactList(list: ArrayList<Contact>) {
        val listOfMaps = list.map {
            mapOf(
                "name" to it.name,
                "number" to it.number
            )
        }.toMutableList()

        val from = listOf(
            "name", "number"
        ).toTypedArray()

        val to = intArrayOf(
            R.id.text_name,
            R.id.text_number
        )

        val adapter = SimpleAdapter(this, listOfMaps, R.layout.list_item, from, to)
        binding.listView.adapter = adapter
    }
}