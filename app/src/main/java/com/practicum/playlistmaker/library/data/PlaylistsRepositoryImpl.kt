package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.library.data.DB.PlaylistDBConverter
import com.practicum.playlistmaker.library.data.DB.PlaylistEntity
import com.practicum.playlistmaker.library.data.DB.PlaylistsDao
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class PlaylistsRepositoryImpl(
    private val appDatabase: PlaylistsDao,
    private val playlistsDbConvertor: PlaylistDBConverter
): PlaylistsRepository {

    override fun addNewPlaylist(playlist: Playlist) {
        runBlocking {
            appDatabase.insertPlaylist(playlistsDbConvertor.map(playlist))
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.getAllPlaylists()
        emit(convertFromEntityToPlaylist(playlists))
    }


    private fun convertFromEntityToPlaylist(playlist: List<PlaylistEntity?>): List<Playlist> {
        return playlist.map { plList -> playlistsDbConvertor.map(plList) }
    }



}