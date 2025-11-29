package com.practicum.playlistmaker.library.ui.activity.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment.Companion.PLAYLIST_SCREEN_FRAGMENT
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistsFragment.Companion.ARGS_PLAYLIST
import com.practicum.playlistmaker.library.ui.viewModel.playlists.OnItemLongClickListener
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistScreenViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.SinglePlaylistState
import com.practicum.playlistmaker.library.ui.viewModel.playlists.TracksInPlaylistAdapter
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.view.OnItemClickListener

import com.practicum.playlistmaker.utils.debounce

import org.koin.android.ext.android.inject

class PlaylistScreenFragment: Fragment() {
    private var fragmentIdentificator = PLAYLIST_SCREEN_FRAGMENT

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistScreenViewModel by inject()

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private  var playlistId: Int  = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            toPlayer(track)
        }
        playlistId = requireArguments().getInt(ARGS_PLAYLIST)
        viewModel.getPlaylistById(playlistId)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
            showPlaylistData()
            showBottomSheet()
        }
        setMenuButtonSheet()
        showSharing()
        binding.backFromPlaylistScreen.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setMenuButtonSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.playlistMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        }
    }

    private fun showTracksList(tracks: List<Track>) {
            binding.tracksRecyclerView.adapter = TracksInPlaylistAdapter(tracks,
                object : OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        onTrackClickDebounce(tracks[position])
                    }
                },
                object : OnItemLongClickListener {
                    override fun onItemLongClick(position: Int) {
                        showDeleteTrackDialog(tracks[position].trackId, playlistId, position)
                    }
                }
            )
            binding.tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSharing(){
        binding.shareTracks.setOnClickListener {
           sharePlaylist(it)
        }
    }

    private fun showBottomSheet(){
        val playlist:Playlist = viewModel.getPlaylist()
        if (playlist.playlistImageDir!=null && playlist.playlistImageDir.toString().isNotEmpty()) {
            binding.image.setImageURI(playlist.playlistImageDir)
            binding.image.setScaleType(CENTER_CROP)
        }
        else {
            binding.image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.placeholder102, null
                )
            )
        }
        binding.name.setText(playlist.playlstName)
        binding.count.setText(viewModel.getTracksCountString())
        binding.sharing.setOnClickListener {
            sharePlaylist(it)
        }
        binding.edit.setOnClickListener {
            editPlaylist(it)
        }
        binding.delete.setOnClickListener {
            showDeletePlaylistDialog()
        }
    }

    private fun sharePlaylist(view:View) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val message = viewModel.makeSharingMessage()
        when {
            message.isNotEmpty()-> viewModel.shareTracks(message)
            else -> Snackbar.make(view,
                requireContext().getString(R.string.no_tracks),
                Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun editPlaylist(it: View?) {
        findNavController().navigate(
            R.id.action_playlistScreenFragment_to_playlistAddingFragment,
            bundleOf(PlaylistAddingFragment.PREVIOUS_FRAGMENT to  fragmentIdentificator,
                PlaylistAddingFragment.PLAYLIST_ID to playlistId)
        )
    }

    private fun showPlaylistData(){
        val playlist:Playlist = viewModel.getPlaylist()
        binding.playlistName.setText(playlist.playlstName)
        binding.playlistDescription.setText(playlist.playlistDescription)
        binding.playlistTracksCount.setText(viewModel.getTracksCountString())
        binding.playlistTime.setText(viewModel.getPlaylistDurationString())
        if (playlist.playlistImageDir!=null && playlist.playlistImageDir.toString().isNotEmpty()) {
            binding.playlistImage.setImageURI(playlist.playlistImageDir)
            binding.playlistImage.setScaleType(CENTER_CROP)
        }
        else {
            binding.playlistImage.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.album_placeholder_512, null
                )
            )
        }
    }

    private fun render(state: SinglePlaylistState) {
        when (state) {
            is SinglePlaylistState.Content -> {
                binding.noTracks.visibility = View.GONE
                binding.tracksRecyclerView.visibility = View.VISIBLE
                showTracksList(state.tracks)
            }
            else -> { binding.noTracks.visibility = View.VISIBLE
                binding.tracksRecyclerView.visibility = View.GONE
            }
        }
    }

    private  fun showDeleteTrackDialog(trackId:Int, playlistId:Int, position:Int){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_track_dialog_hadder))
            .setNeutralButton(requireContext().getString(R.string.no)) { dialog, which ->
            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                viewModel.deleteTrack(trackId, playlistId)
                binding.tracksRecyclerView.adapter?.notifyItemRemoved(position)
            }
            .show()
    }

    private  fun showDeletePlaylistDialog(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_playlist_dialog_hadder))
            .setMessage(requireContext().getString(R.string.delete_playlist_dialog_message))
            .setNeutralButton(requireContext().getString(R.string.no)) { dialog, which ->
            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun toPlayer(item: Track) {
        findNavController().navigate(
            R.id.action_playlistScreenFragment_to_playerFragment,
            bundleOf(PlayerFragment.ARGS_TRACK to item)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}