package com.practicum.playlistmaker.data.Repositories

import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.domain.interfaces.repositories.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track


class SearchHistoryRepositoryImpl( private val storage: StorageClient<MutableList<Track>>): SearchHistoryRepository {
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

