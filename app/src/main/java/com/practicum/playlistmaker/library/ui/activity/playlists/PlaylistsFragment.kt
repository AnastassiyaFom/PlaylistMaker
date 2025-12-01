package com.practicum.playlistmaker.library.ui.activity.playlists



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment.Companion.PLAYLISTS_FRAGMENT
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistsState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistsViewModel
import com.practicum.playlistmaker.utils.debounce
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {
    private var fragmentIdentificator = PLAYLISTS_FRAGMENT

    private var _binding: FragmentPlaylistsBinding? = null
    // создаём неизменяемую переменную, к которой можно будет обращаться без ?. Мы должны не забыть инициализировать _binding, до того как использовать
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by inject()
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

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
                R.id.action_mediatechFragment_to_playlistAddingFragment,
                bundleOf(PlaylistAddingFragment.PREVIOUS_FRAGMENT to fragmentIdentificator)
            )
        }
        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
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
        binding.playlistsRecyclerView.visibility=View.VISIBLE
        binding.playlistsRecyclerView.adapter = PlaylistsAdapter(playlists,object : OnPlaylistItemClickListener {
            override fun onItemClick(position: Int) {
                onPlaylistClickDebounce(playlists[position])
            }
        })
        onPlaylistClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist: Playlist ->
            findNavController().navigate(
            R.id.action_mediatechFragment_to_playlistScreenFragment,
            bundleOf(ARGS_PLAYLIST to playlist.id)
        )
        }
    }

    private fun showEmpty() {
        binding.errorNoPlaylists.visibility=View.VISIBLE
        binding.playlistsRecyclerView.visibility=View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance() = PlaylistsFragment().apply {
        }
            const val ARGS_PLAYLIST = "playlist ID"
            const val CLICK_DEBOUNCE_DELAY=500L
    }
}