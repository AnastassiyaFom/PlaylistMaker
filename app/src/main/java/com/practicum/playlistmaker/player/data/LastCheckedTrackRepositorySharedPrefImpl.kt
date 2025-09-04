package com.practicum.playlistmaker.player.data

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