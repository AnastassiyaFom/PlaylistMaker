package com.practicum.playlistmaker.library.ui.activity



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.ui.viewModel.PlaylistsState
import com.practicum.playlistmaker.library.ui.viewModel.PlaylistsViewModel
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {


    private var _binding: FragmentPlaylistsBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.newPlaylist.setOnClickListener{
            findNavController().navigate(
                R.id.action_mediatechFragment_to_playlistAddingFragment
            )
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
    private fun render(state: PlaylistsState?) {
        when (state){
            is PlaylistsState.Empty->showEmpty()
            is PlaylistsState.Content->showContent(state.playlists)
            else ->showEmpty()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.errorNoPlaylists.visibility=View.GONE
        binding.recyclerView.visibility=View.VISIBLE
        binding.recyclerView.adapter = PlaylistsAdapter(playlists)
    }

    private fun showEmpty() {
        binding.errorNoPlaylists.visibility=View.VISIBLE
        binding.recyclerView.visibility=View.GONE
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
        }
    }
}