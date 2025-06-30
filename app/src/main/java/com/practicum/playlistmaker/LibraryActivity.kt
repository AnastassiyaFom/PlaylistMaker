package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences

import android.content.res.Resources
import android.os.Bundle

import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.SearchActivity.Companion.CHECKED_TRACK



class LibraryActivity : AppCompatActivity() {

    // Интерактивные элементы экрана
    private val backButton by lazy { findViewById<ImageView>(R.id.backFromLibrary) }
    private val trackNameView: TextView by lazy { findViewById<TextView>(R.id.track_name_library) }
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
        var  sharedPrefs: SharedPreferences =  getSharedPreferences(CHECKED_TRACK, MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        if (savedInstanceState != null) {
            this.onRestoreInstanceState(savedInstanceState)
        }
        checkedTrack=intent.getParcelableExtra<Track>(CHECKED_TRACK)
        if (checkedTrack==null){

            val json:String? = if (sharedPrefs==null) null else sharedPrefs.getString(CHECKED_TRACK, "")
            if (!json.isNullOrEmpty()) {
                checkedTrack = Gson().fromJson(json, object : TypeToken<Track>() {}.type )
            }
        }

        backButton.setOnClickListener {
            if (checkedTrack!=null){
                var json:String?=Gson().toJson(checkedTrack)
                sharedPrefs.edit()
                    .remove(CHECKED_TRACK)
                    .putString(CHECKED_TRACK, json)
                    .apply()
            }
            this.finish()
        }

        trackNameView.text = checkedTrack?.trackName ?: ""
        artistNameView.text = checkedTrack?.artistName ?: ""
        trackTimeView.text = checkedTrack?.trackTime ?: ""

        var artworkUrl512: String = enlargeImageUrl(checkedTrack?.artworkUrl100)
        Glide.with(applicationContext)
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder_512)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(8f)))
            .into(albumImageView)
        if (checkedTrack?.collectionName.isNullOrEmpty()) {
            collectionNameView.visibility= View.GONE
            collectionTextView.visibility= View.GONE
        }
        else {
            collectionNameView.text = checkedTrack?.collectionName
            collectionTextView.visibility= View.VISIBLE
        }
        if (checkedTrack?.releaseDate.isNullOrEmpty()) {
            releaseDateView.visibility= View.GONE
            releaseTextView.visibility= View.GONE
        }
        else {
            releaseDateView.text = checkedTrack?.releaseDate
            releaseTextView.visibility= View.VISIBLE
        }

        // primaryGenreNameView.text = checkedTrack?.primaryGenreName

       // countryView.text = checkedTrack?.country

        if (checkedTrack?.primaryGenreName.isNullOrEmpty()) {
            primaryGenreNameView.visibility= View.GONE
            primaryGenreTextView.visibility= View.GONE
        }
        else {
            primaryGenreNameView.text = checkedTrack?.primaryGenreName
            primaryGenreTextView.visibility= View.VISIBLE
        }
        if (checkedTrack?.country.isNullOrEmpty()) {
            countryView.visibility= View.GONE
            countryTextView.visibility= View.GONE
        }
        else {
            countryView.text = checkedTrack?.country
            countryTextView.visibility= View.VISIBLE
        }



    }

    private fun enlargeImageUrl(artworkUrl: String?): String {
        if (artworkUrl != null) {
            return artworkUrl.replaceAfterLast('/',"512x512bb.jpg")
        }
        return artworkUrl.toString()
    }
    private fun dpToPixel(dp: Float): Int {
            val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
            val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            return Math.round(px).toInt()
    }
}