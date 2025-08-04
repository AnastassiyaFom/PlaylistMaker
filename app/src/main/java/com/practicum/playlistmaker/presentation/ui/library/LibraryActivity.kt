package com.practicum.playlistmaker.presentation.ui.library

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator.provideLastCheckedTrackInteractor
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.interfaces.interactors.LastCheckedTrackInteractor
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity.Companion.CHECKED_TRACK
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class LibraryActivity : AppCompatActivity() {

    // Интерактивные элементы экрана
    private val backButton by lazy { findViewById<ImageView>(R.id.backFromLibrary) }
    private val playButton by lazy { findViewById<ImageView>(R.id.play_button)}
    private val trackNameView: TextView by lazy { findViewById<TextView>(R.id.track_name_library) }
    private val playingTrackTime: TextView by lazy { findViewById<TextView>(R.id.playing_track_time) }
    private val artistNameView: TextView by lazy { findViewById<TextView>(R.id.artist_name_library) }
    private val trackTimeView: TextView by lazy { findViewById<TextView>(R.id.duration_data) }
    private val albumImageView: ImageView by lazy { findViewById<ImageView>(R.id.track_image_library) }
    private val collectionNameView: TextView by lazy { findViewById<TextView>(R.id.collection_data) }
    private val releaseDateView: TextView by lazy { findViewById<TextView>(R.id.year_data) }
    private val primaryGenreNameView: TextView by lazy { findViewById<TextView>(R.id.genre_data) }
    private val countryView: TextView by lazy { findViewById<TextView>(R.id.country_data) }
    private val releaseTextView: TextView by lazy { findViewById<TextView>(R.id.year_text) }
    private val collectionTextView: TextView by lazy { findViewById<TextView>(R.id.collection_text) }
    private val primaryGenreTextView: TextView by lazy { findViewById<TextView>(R.id.genre_text) }
    private val countryTextView: TextView by lazy { findViewById<TextView>(R.id.country_text) }

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
        setContentView(R.layout.activity_library)
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
        backButton.setOnClickListener {
            if (checkedTrack!=null) {
                lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack!!)
            }
            this.finish()
        }

        // Отрисовываем  экран с данными о треке
        trackNameView.text = checkedTrack?.trackName ?: ""
        artistNameView.text = checkedTrack?.artistName ?: ""
        trackTimeView.text = checkedTrack?.trackTime ?: ""

        var artworkUrl512: String = checkedTrack?.artworkUrl500.toString()
        Glide.with(applicationContext)
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder_512)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(8f)))
            .into(albumImageView)
        if (checkedTrack?.collectionName.isNullOrEmpty()) {
            collectionNameView.visibility = View.GONE
            collectionTextView.visibility = View.GONE
        } else {
            collectionNameView.text = checkedTrack?.collectionName
            collectionTextView.visibility = View.VISIBLE
        }

        if (checkedTrack?.releaseDate.isNullOrEmpty()) {
            releaseDateView.visibility = View.GONE
            releaseTextView.visibility = View.GONE
        } else {
            releaseDateView.text = checkedTrack?.releaseDate
            releaseTextView.visibility = View.VISIBLE
        }

        if (checkedTrack?.primaryGenreName.isNullOrEmpty()) {
            primaryGenreNameView.visibility = View.GONE
            primaryGenreTextView.visibility = View.GONE
        } else {
            primaryGenreNameView.text = checkedTrack?.primaryGenreName
            primaryGenreTextView.visibility = View.VISIBLE
        }

        if (checkedTrack?.country.isNullOrEmpty()) {
            countryView.visibility = View.GONE
            countryTextView.visibility = View.GONE
        } else {
            countryView.text = checkedTrack?.country
            countryTextView.visibility = View.VISIBLE
        }

        // Воспроизводим трек
        preparePlayer()
        playButton.setOnClickListener {
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
        playingTrackTime.text="00:00"

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
               playButton.isEnabled = true
                playerState = PlayerState.STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {
                    playButton.setImageResource(R.drawable.play_button_play)
                    playerState = PlayerState.STATE_PREPARED
                    handler.removeCallbacks(playingProgressRunnable)
                    playingTrackTime.text = "00:00"
                    mediaPlayer.seekTo(0)
            }

        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.play_button_pause)
        playerState = PlayerState.STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button_play)
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
            playingTrackTime.text= SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(playingProgressRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
        }
    }

    companion object {
        private const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L
    }
}
