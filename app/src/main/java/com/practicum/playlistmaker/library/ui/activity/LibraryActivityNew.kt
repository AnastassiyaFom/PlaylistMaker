package com.practicum.playlistmaker.library.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediatechBinding
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.player.ui.activity.Playlists
import com.practicum.playlistmaker.player.ui.activity.SelectedTracks

class LibraryActivityNew: AppCompatActivity()  {
    @SuppressLint("WrongViewCast", "MissingInflatedId")

    private lateinit var binding:ActivityMediatechBinding
    private var fragmentSelectedFlag: FragmentSelectionFlag = FragmentSelectionFlag.SELECTED_TRACKS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediatechBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null ) {
            showSelectedTracks()
        }
        setOnClickListeners(this)
    }

    fun showSelectedTracks(){
        binding.selectedTracksLine.visibility = View.VISIBLE
        binding.playlistsLine.visibility = View.GONE
        // реализация через вызов методов
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentСontainerView, SelectedTracks())
            .commit()
    }
    fun showPlaylist(){
        binding.selectedTracksLine.visibility = View.GONE
        binding.playlistsLine.visibility = View.VISIBLE
        // реализация через лямбду
        supportFragmentManager.commit {
            add<Playlists>(R.id.fragmentСontainerView)
        }
    }
    private fun setOnClickListeners(context: Context) {
        binding.selectedTracks.setOnClickListener {
            fragmentSelectedFlag = FragmentSelectionFlag.SELECTED_TRACKS
            binding.selectedTracksLine.visibility = View.VISIBLE
            binding.playlistsLine.visibility = View.GONE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentСontainerView, SelectedTracks())
                .setReorderingAllowed(true)
                .commit()
        }
        binding.playlists.setOnClickListener {
            fragmentSelectedFlag = FragmentSelectionFlag.PLAYLISTS
            binding.selectedTracksLine.visibility = View.GONE
            binding.playlistsLine.visibility = View.VISIBLE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentСontainerView, Playlists())
                .setReorderingAllowed(true)
                .commit()
        }
        binding.backToMainFromLibrary.setOnClickListener {
            val butBackClickListener = Intent(context, MainActivity::class.java)
            butBackClickListener.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.finish()
        }

    }



}