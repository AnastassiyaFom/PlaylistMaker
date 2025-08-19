package com.practicum.playlistmaker.presentation.ui.library

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.creator.Creator.provideLastCheckedTrackInteractor
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding
import com.practicum.playlistmaker.domain.interfaces.interactors.LastCheckedTrackInteractor
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity.Companion.CHECKED_TRACK
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class LibraryActivity : AppCompatActivity() {

    // Интерактивные элементы экрана
    private lateinit var binding: ActivityLibraryBinding

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    private lateinit var  lastCheckedTrackInteractor: LastCheckedTrackInteractor

    private  var checkedTrack: Track? = null

    private val handler = Handler(Looper.getMainLooper())
    private val playingProgressRunnable = Runnable { displayTime() }

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


        lastCheckedTrackInteractor = provideLastCheckedTrackInteractor(this)

        // Получаем данные о треке из intent (с активити поиска) или  из sharedPreferences,
        // если поиска еще не было в текущей сессии
        checkedTrack = intent.getParcelableExtra<Track>(CHECKED_TRACK)
        if (checkedTrack == null) {
            checkedTrack = lastCheckedTrackInteractor.getLastCheckedTrack()
        }

        // Возврат в предыдущую активити
        binding.backFromLibrary.setOnClickListener {
            if (checkedTrack!=null) {
                lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack!!)
            }
            this.finish()
        }

        // Отрисовываем  экран с данными о треке
        binding.trackNameLibrary.text = checkedTrack?.trackName ?: ""
        binding.artistNameLibrary.text = checkedTrack?.artistName ?: ""
        binding.durationData.text = checkedTrack?.trackTime ?: ""

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
        preparePlayer()
        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        if (checkedTrack!=null) {
            lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack!!)
        }
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(playingProgressRunnable)
        binding.playingTrackTime.text="00:00"

    }


    private fun dpToPixel(dp: Float): Int {
            val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
            val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return Math.round(px).toInt()
    }

    // Функции для работы с плеером
    private fun preparePlayer() {

        if (!checkedTrack?.previewUrl.isNullOrEmpty()) {
            mediaPlayer.setDataSource(checkedTrack?.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                binding.playButton.isEnabled = true
                playerState = PlayerState.STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {
                    binding.playButton.setImageResource(R.drawable.play_button_play)
                    playerState = PlayerState.STATE_PREPARED
                    handler.removeCallbacks(playingProgressRunnable)
                    binding.playingTrackTime.text = "00:00"
                    mediaPlayer.seekTo(0)
            }

        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.play_button_pause)
        playerState = PlayerState.STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button_play)
        playerState = PlayerState.STATE_PAUSED
    }
    private fun playbackControl() {

        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
                handler.removeCallbacks(playingProgressRunnable)
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                handler.postDelayed(playingProgressRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
            }
            else->{}
        }
    }

    private fun displayTime(){
        if (playerState == PlayerState.STATE_PLAYING){
            val currentPosition = mediaPlayer.getCurrentPosition().toLong()
            binding.playingTrackTime.text= SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(playingProgressRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
        }
    }

    companion object {
        private const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L
    }
}
