package com.practicum.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding
import com.practicum.playlistmaker.player.ui.viewModel.LibraryViewModel
import com.practicum.playlistmaker.search.ui.view.SearchActivity.Companion.CHECKED_TRACK
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.player.ui.viewModel.PlayerState
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private  var mediaPlayerViewModel : PlayerViewModel?=null
    private lateinit var viewModel : LibraryViewModel
    private  var checkedTrack: Track? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CHECKED_TRACK, checkedTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        checkedTrack = savedInstanceState.getParcelable(CHECKED_TRACK)!!
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }
        viewModel = ViewModelProvider(this, LibraryViewModel.getFactory(intent))
            .get(LibraryViewModel::class.java)
        viewModel?.observeCheckedTrack()?.observe(this) {
            checkedTrack=it
        }
        checkedTrack=viewModel.loadTrack()
        if (checkedTrack != null && !checkedTrack?.previewUrl.isNullOrEmpty()){
            mediaPlayerViewModel = ViewModelProvider(this,
                PlayerViewModel.getFactory(checkedTrack?.previewUrl!!)
            )
                .get(PlayerViewModel::class.java)

            // Подписываемся на поля плеера
            mediaPlayerViewModel?.observeProgressTime()?.observe(this) {
                binding.playingTrackTime.text = it
            }
            mediaPlayerViewModel?.observePlayerState()?.observe(this) {
                changeButtonIcon(it == PlayerState.STATE_PLAYING)
                enableButton(it != PlayerState.STATE_DEFAULT)
            }
        }
        // Возврат в предыдущую активити
        binding.backFromLibrary.setOnClickListener {
            viewModel.saveTrack()
            this.finish()
        }
        // Отрисовываем  экран с данными о треке
        binding.apply{
            trackNameLibrary.text = checkedTrack?.trackName ?: ""
            artistNameLibrary.text = checkedTrack?.artistName ?: ""
            durationData.text = checkedTrack?.trackTime ?: ""
        }

        var artworkUrl512: String = checkedTrack?.artworkUrl500.toString()
        Glide.with(applicationContext)
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
            mediaPlayerViewModel?.onPlayButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayerViewModel!=null)  mediaPlayerViewModel?.onPause()
    }

    override fun onDestroy() {
        viewModel.saveTrack()
        super.onDestroy()
    }
    private fun enableButton(isEnabled: Boolean) {
        binding.playButton.isEnabled = isEnabled
    }

    private fun changeButtonIcon(isPlaying: Boolean) {
        if (isPlaying){
            binding.playButton.setImageResource( R.drawable.play_button_pause)
        } else {
            binding.playButton.setImageResource(R.drawable.play_button_play)
        }
    }
    private fun dpToPixel(dp: Float): Int {
            val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
            val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return Math.round(px).toInt()
    }
}
