package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.StorageClient
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.Track


class SearchHistoryRepositoryImpl( private val storage: StorageClient<MutableList<Track>>):
    SearchHistoryRepository {
    override val tracksInHistoryMaxLength: Int = 10
    private var tracksInHistory: MutableList<Track> = storage.getData() ?: mutableListOf()

    override fun getTracksFromHistory():MutableList<Track>{
        return tracksInHistory
    }
    override fun writeDataToRepository(data:MutableList<Track>) {
        tracksInHistory=data
        storage.storeData(data)
    }

    override fun clearHistory() {
        tracksInHistory.clear()
        storage.clearStorage()
    }


}

