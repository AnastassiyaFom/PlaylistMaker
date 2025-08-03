package com.practicum.playlistmaker.data.Repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.interfaces.repositories.LastCheckedTrackRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity.Companion.CHECKED_TRACK

class LastCheckedTrackRepositorySharedPrefImpl(override val context: Context):
    LastCheckedTrackRepository {

    private var sharedPrefs: SharedPreferences = context.getSharedPreferences(CHECKED_TRACK, MODE_PRIVATE)

    override fun getLastCheckedTrack():Track? {
        var track:Track? = null
        val json: String? =
            if (sharedPrefs == null) null else sharedPrefs.getString(CHECKED_TRACK, "")
         if (!json.isNullOrEmpty()) {
             track = Gson().fromJson(json, object : TypeToken<Track>() {}.type)
        }
        return track
    }

    override fun saveLastCheckedTrack(track:Track) {
       // if (track != null) {
            var json: String? = Gson().toJson(track)
            sharedPrefs.edit()
                .remove(CHECKED_TRACK)
                .putString(CHECKED_TRACK, json)
                .apply()
       // }
    }
}