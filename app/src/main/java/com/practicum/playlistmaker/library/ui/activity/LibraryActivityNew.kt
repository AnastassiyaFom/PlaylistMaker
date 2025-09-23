package com.practicum.playlistmaker.library.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediatechBinding
import com.practicum.playlistmaker.main.ui.MainActivity


class LibraryActivityNew: AppCompatActivity()  {
    @SuppressLint("WrongViewCast", "MissingInflatedId")

    private lateinit var binding:ActivityMediatechBinding
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediatechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        setOnClickListeners(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.setText(R.string.selected_tracks)
                1 -> tab.setText(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun setOnClickListeners(context: Context) {
        binding.backToMainFromLibrary.setOnClickListener {
            val butBackClickListener = Intent(context, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }
    }

}