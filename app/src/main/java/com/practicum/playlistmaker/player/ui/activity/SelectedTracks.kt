package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.practicum.playlistmaker.search.domain.Track

class SelectedTracks : Fragment() {
    //переменная-заглушка
    private val  selectedTracks:ArrayList<Track>?=null
    private var _binding: FragmentSelectedTracksBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        if (selectedTracks==null){
            binding.errorNoSelectedTracks.visibility = View.VISIBLE
        }
        else
        {
            binding.errorNoSelectedTracks.visibility = View.GONE
        }
        return binding.root
    }

}