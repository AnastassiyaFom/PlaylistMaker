package com.practicum.playlistmaker.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity
import com.practicum.playlistmaker.presentation.ui.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.ui.library.LibraryActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val butSearch = findViewById<Button>(R.id.search)
        val butSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displaySearchActivity = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearchActivity)
            }
        }
        butSearch.setOnClickListener(butSearchClickListener)

        val butLib = findViewById<Button>(R.id.library)
        butLib.setOnClickListener {
            val displayLibraryActivity = Intent(this, LibraryActivity::class.java)
            startActivity(displayLibraryActivity)
        }

        val butSettings = findViewById<Button>(R.id.settings)
        butSettings.setOnClickListener {
            val displaySettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsActivity)
        }


    }
}