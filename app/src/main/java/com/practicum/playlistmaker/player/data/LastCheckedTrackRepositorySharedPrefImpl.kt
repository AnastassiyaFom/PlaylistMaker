package com.practicum.playlistmaker.player.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.LastCheckedTrackRepository
import com.practicum.playlistmaker.search.data.PrefsStorageClient
import com.practicum.playlistmaker.search.domain.Track

class LastCheckedTrackRepositorySharedPrefImpl( private val storageClient: PrefsStorageClient<Track?>
): LastCheckedTrackRepository {
    override fun getLastCheckedTrack(): Track? {
        return storageClient.getData()
    }

    override fun saveLastCheckedTrack(track: Track) {
        storageClient.storeData(track)
    }


}