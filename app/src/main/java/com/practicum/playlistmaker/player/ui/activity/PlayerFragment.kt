package com.practicum.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment.Companion.PLAYER_FRAGMENT
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistsState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.utils.debounce

import org.koin.android.ext.android.inject

class PlayerFragment : Fragment() {
    private var fragmentIdentificator = PLAYER_FRAGMENT
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val viewModel : PlayerViewModel by inject()
    private  var checkedTrack: Track =Track()
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeCheckedTrack().observe(viewLifecycleOwner) {
            checkedTrack=it
        }
        checkedTrack = viewModel.loadTrack(requireArguments().getParcelable(ARGS_TRACK)?:Track())


        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            setPlayButtonIcon(it.isButtonPlay)
            enableButton(it.isPlayButtonEnabled)
            binding.playingTrackTime.text = it.progress
            if (!(it is PlayerState.Playing)) binding.playButton.setImageResource(R.drawable.play_button_play)
        }

        // Возврат в предыдущую активити
        binding.backFromLibrary.setOnClickListener {
            findNavController().navigateUp()
        }

        // Отрисовываем  экран с данными о треке
        binding.apply{
            trackNameLibrary.text = checkedTrack.trackName ?: ""
            artistNameLibrary.text = checkedTrack.artistName ?: ""
            durationData.text = checkedTrack.trackTime ?: ""
        }
        var setTrackToFavorites: Boolean = false

        if (checkedTrack.isEmpty())
            setLikeButtonImage(setTrackToFavorites)
        else {
            setTrackToFavorites = viewModel.isTrackInFavorites(checkedTrack)
            setLikeButtonImage(setTrackToFavorites)
        }

        // Добавление/удаление трека из избранного
        binding.like.setOnClickListener {
            setTrackToFavorites = !setTrackToFavorites
            setLikeButtonImage(setTrackToFavorites)
            if (setTrackToFavorites) {
                viewModel.addToFavorites(checkedTrack)
            } else {
                viewModel.deleteFromFavorites(checkedTrack)
            }

        }

        showTrackData()

        // Воспроизводим трек
        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        // Для выбора плейлиста
        showBottomSheet()
    }



    private fun showBottomSheet() {
        //val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.addToLibrary.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            viewModel.getPlaylists()
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_playlistAddingFragment,
                bundleOf(PlaylistAddingFragment.PREVIOUS_FRAGMENT to fragmentIdentificator)
            )
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        onPlaylistClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            addTrackToPlaylist(playlist)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun enableButton(isEnabled: Boolean) {
        binding.playButton.isEnabled = isEnabled
    }

    private fun setPlayButtonIcon(isButtonPlay: Boolean) {
        if (isButtonPlay){
            binding.playButton.setImageResource(R.drawable.play_button_play)
        } else {
            binding.playButton.setImageResource(R.drawable.play_button_pause)
        }
    }

    private fun setLikeButtonImage(isTrackInFavorites: Boolean) {
        if (isTrackInFavorites){
            //удалить трек из избранного
            binding.like.setImageResource(R.drawable.like_selected)
        }
        else {
            //добавить трек в избранное
            binding.like.setImageResource(R.drawable.like_unselected)
        }
    }


    private fun dpToPixel(dp: Float): Int {
        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(px).toInt()
    }


    private fun addTrackToPlaylist (playlist:Playlist){
        val isTrackInPlaylist: Boolean = viewModel.isTrackInPlaylist(checkedTrack, playlist)
        binding.recyclerView.let {
            if (isTrackInPlaylist) {
                Snackbar.make(
                    it,
                    "Трек уже добавлен в плейлист ${playlist.playlstName}",
                    Snackbar.LENGTH_LONG
                ).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            else{
                viewModel.addTrackToPlaylist(checkedTrack, playlist)
                Snackbar.make(
                    it,
                    "Добавлено в плейлист ${playlist.playlstName}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }
    private fun render(state: PlaylistsState?) {
        when (state){
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlists)
            else -> showEmpty()
        }
    }
    private fun showContent(playlists: List<Playlist>) {
        binding.recyclerView.visibility=View.VISIBLE
        binding.recyclerView.adapter =
            PlaylistAddingTrackAdapter(playlists, object : OnPlaylistItemClickListener {
                override fun onItemClick(position: Int) {
                    onPlaylistClickDebounce(playlists[position])
                }
            })
    }

    private fun showEmpty() {
        binding.recyclerView.visibility=View.GONE
    }

    private fun showTrackData() {

        val artworkUrl512: String = checkedTrack.artworkUrl500
        Glide.with(requireContext())
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder_512)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(8f)))
            .into(binding.trackImageLibrary)
        if (checkedTrack.collectionName.isEmpty()) {
            binding.collectionData.visibility = View.GONE
            binding.collectionText.visibility = View.GONE
        } else {
            binding.collectionData.text = checkedTrack.collectionName
            binding.collectionText.visibility = View.VISIBLE
        }

        if (checkedTrack.releaseDate.isEmpty()) {
            binding.yearData.visibility = View.GONE
            binding.yearText.visibility = View.GONE
        } else {
            binding.yearData.text = checkedTrack.releaseDate
            binding.yearText.visibility = View.VISIBLE
        }

        if (checkedTrack.primaryGenreName.isEmpty()) {
            binding.genreData.visibility = View.GONE
            binding.genreText.visibility = View.GONE
        } else {
            binding.genreData.text = checkedTrack.primaryGenreName
            binding.genreText.visibility = View.VISIBLE
        }

        if (checkedTrack.country.isEmpty()) {
            binding.countryData.visibility = View.GONE
            binding.countryText.visibility = View.GONE
        } else {
            binding.countryData.text = checkedTrack.country
            binding.countryText.visibility = View.VISIBLE
        }
    }

    companion object {
        const val ARGS_TRACK = "track"
        const val CLICK_DEBOUNCE_DELAY=500L
    }
}
