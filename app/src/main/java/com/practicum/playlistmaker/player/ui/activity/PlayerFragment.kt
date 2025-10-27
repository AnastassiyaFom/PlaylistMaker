package com.practicum.playlistmaker.player.ui.activity

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.ui.viewModel.PlayerState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.Track
import org.koin.android.ext.android.inject

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel : PlayerViewModel by inject()
    private  var checkedTrack: Track? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeCheckedTrack().observe(viewLifecycleOwner) {
            checkedTrack=it
        }
        checkedTrack = viewModel.loadTrack(requireArguments().getParcelable(ARGS_TRACK))

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
                setButtonIcon(it.isButtonPlay)
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
            trackNameLibrary.text = checkedTrack?.trackName ?: ""
            artistNameLibrary.text = checkedTrack?.artistName ?: ""
            durationData.text = checkedTrack?.trackTime ?: ""
        }

        val artworkUrl512: String = checkedTrack?.artworkUrl500.toString()
        Glide.with(requireContext())
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder_512)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(8f)))
            .into(binding.trackImageLibrary)
        if (checkedTrack?.collectionName.isNullOrEmpty()) {
            binding.collectionData.visibility = View.GONE
            binding.collectionText.visibility = View.GONE
        } else {
            binding.collectionData.text = checkedTrack?.collectionName
            binding.collectionText.visibility = View.VISIBLE
        }

        if (checkedTrack?.releaseDate.isNullOrEmpty()) {
            binding.yearData.visibility = View.GONE
            binding.yearText.visibility = View.GONE
        } else {
            binding.yearData.text = checkedTrack?.releaseDate
            binding.yearText.visibility = View.VISIBLE
        }

        if (checkedTrack?.primaryGenreName.isNullOrEmpty()) {
            binding.genreData.visibility = View.GONE
            binding.genreText.visibility = View.GONE
        } else {
            binding.genreData.text = checkedTrack?.primaryGenreName
            binding.genreText.visibility = View.VISIBLE
        }

        if (checkedTrack?.country.isNullOrEmpty()) {
            binding.countryData.visibility = View.GONE
            binding.countryText.visibility = View.GONE
        } else {
            binding.countryData.text = checkedTrack?.country
            binding.countryText.visibility = View.VISIBLE
        }

        // Воспроизводим трек
        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun enableButton(isEnabled: Boolean) {
        binding.playButton.isEnabled = isEnabled
    }

    private fun setButtonIcon(isButtonPlay: Boolean) {
        if (isButtonPlay){
            binding.playButton.setImageResource(R.drawable.play_button_play)
        } else {
            binding.playButton.setImageResource(R.drawable.play_button_pause)
        }
    }
    private fun dpToPixel(dp: Float): Int {
            val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
            val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return Math.round(px).toInt()
    }
    companion object {
        const val ARGS_TRACK = "track"
    }
}
