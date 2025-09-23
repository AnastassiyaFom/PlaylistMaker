package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.practicum.playlistmaker.search.domain.Track

class Playlists : Fragment() {
    //переменная-заглушка, тип данных не тот
    private val  playlists:ArrayList<Track>?=null
    private var _binding: FragmentPlaylistsBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        if (playlists==null){
            binding.errorNoSelectedTracks.visibility = View.VISIBLE
        }
        else
        {
            binding.errorNoSelectedTracks.visibility = View.GONE
        }
        return binding.root
    }
}