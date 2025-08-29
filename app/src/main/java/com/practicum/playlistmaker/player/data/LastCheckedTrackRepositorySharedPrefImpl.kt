package com.practicum.playlistmaker.player.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.LastCheckedTrackRepository
import com.practicum.playlistmaker.search.domain.Track

class LastCheckedTrackRepositorySharedPrefImpl(val context: Context):
    LastCheckedTrackRepository {

    private val gson = Gson()
   private lateinit var  sharedPrefs: SharedPreferences
    init {
        try {
            sharedPrefs =  context.getSharedPreferences (CHECKED_TRACK, MODE_PRIVATE)
        }
        catch(e: Exception){}
    }

    override fun getLastCheckedTrack(): Track? {
        var track: Track? = null
        val json: String? =
            if (sharedPrefs == null) null else sharedPrefs.getString(CHECKED_TRACK, "")
         if (!json.isNullOrEmpty()) {
             track = gson.fromJson(json, object : TypeToken<Track>() {}.type)
        }
        return track
    }

    override fun saveLastCheckedTrack(track: Track) {
            val json: String? = gson.toJson(track)
            sharedPrefs.edit()
                .remove(CHECKED_TRACK)
                .putString(CHECKED_TRACK, json)
                .apply()
    }
    companion object{
        private   const val CHECKED_TRACK = "CHECKED_TRACK"
    }
}