package com.practicum.playlistmaker.library.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.practicum.playlistmaker.library.ui.viewModel.SelectedTracksState
import com.practicum.playlistmaker.library.ui.viewModel.SelectedTracksViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.view.OnItemClickListener
import com.practicum.playlistmaker.search.ui.view.TracksAdapter
import com.practicum.playlistmaker.utils.debounce
import org.koin.android.ext.android.inject

class SelectedTracksFragment : Fragment() {

    private var _binding: FragmentSelectedTracksBinding? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    private val viewModel: SelectedTracksViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }
    private fun render(state: SelectedTracksState) {
        when (state){
            is SelectedTracksState.Empty->showEmpty()
            is SelectedTracksState.Content->showContent(state.tracks)
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.errorNoSelectedTracks.visibility = View.GONE
        binding.selectedTracksList.visibility = View.VISIBLE
        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false)
        { track ->
            toPlayer(track)
        }
        binding.selectedTracksList.adapter = TracksAdapter(tracks, object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Для предотвращения двойных нажатий на элемент
                onTrackClickDebounce(tracks[position])
            }
        })
        binding.selectedTracksList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showEmpty() {
        binding.errorNoSelectedTracks.visibility = View.VISIBLE
        binding.selectedTracksList.visibility = View.GONE
    }

    private fun toPlayer(item: Track) {
        findNavController().navigate(
            R.id.action_mediatechFragment_to_playerFragment,
            bundleOf(PlayerFragment.ARGS_TRACK to item)
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.selectedTracksList.adapter = null
        _binding=null
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() =SelectedTracksFragment().apply {
        }
    }

}