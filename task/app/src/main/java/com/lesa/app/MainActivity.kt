package com.lesa.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lesa.app.chat.ChatFragment
import com.lesa.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, ChatFragment(), ChatFragment.TAG)
                .addToBackStack(null)
                .commit()


        }
    }

}