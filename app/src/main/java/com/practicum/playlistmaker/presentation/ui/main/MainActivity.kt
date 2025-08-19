package com.practicum.playlistmaker.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity
import com.practicum.playlistmaker.presentation.ui.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.ui.library.LibraryActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val butSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displaySearchActivity = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchActivity)
            }
        }
        binding.search.setOnClickListener(butSearchClickListener)
        binding.library.setOnClickListener {
            val displayLibraryActivity = Intent(this, LibraryActivity::class.java)
            startActivity(displayLibraryActivity)
        }
        binding.settings.setOnClickListener {
            val displaySettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsActivity)
        }
    }
}