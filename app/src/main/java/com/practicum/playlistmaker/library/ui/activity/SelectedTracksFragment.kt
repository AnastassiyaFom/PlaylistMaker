package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.practicum.playlistmaker.library.ui.viewModel.SelectedTracksViewModel
import com.practicum.playlistmaker.search.domain.Track
import org.koin.android.ext.android.inject

class SelectedTracksFragment : Fragment() {
    //переменная-заглушка, будет переписана в следющем спринте
    private val  selectedTracks:ArrayList<Track>?=null
    private var _binding: FragmentSelectedTracksBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    private val viewModel: SelectedTracksViewModel by inject()

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // реализация появится в следующем  спринте
    }

    companion object {
        fun newInstance() =SelectedTracksFragment().apply {
            // позже сюда будет передаваться список избранных треков

        }
    }

}