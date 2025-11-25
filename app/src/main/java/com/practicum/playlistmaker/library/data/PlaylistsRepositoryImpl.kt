package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.library.data.DB.PlaylistDBConverter
import com.practicum.playlistmaker.library.data.DB.PlaylistEntity
import com.practicum.playlistmaker.library.data.DB.PlaylistTrackDAO
import com.practicum.playlistmaker.library.data.DB.PlaylistTrackEntity
import com.practicum.playlistmaker.library.data.DB.PlaylistsDao
import com.practicum.playlistmaker.library.data.DB.TrackDbConvertor
import com.practicum.playlistmaker.library.data.DB.TrackEntity
import com.practicum.playlistmaker.library.data.DB.TracksDao
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class PlaylistsRepositoryImpl(
    private val playlistsTable: PlaylistsDao,
    private val tracksTable: TracksDao,
    private val playlistTrackTable: PlaylistTrackDAO,
    private val playlistsDbConvertor: PlaylistDBConverter,
    private val tracksDBConverter: TrackDbConvertor
): PlaylistsRepository {

    override fun addNewPlaylist(playlist: Playlist) {
        runBlocking {
            playlistsTable.insertPlaylist(playlistsDbConvertor.map(playlist))
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistsTable.getAllPlaylists()
        emit(convertFromEntityToPlaylist(playlists))
    }

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        // Нужна ли здесь проверка на несуществующий плейлист?
        val plId=playlist.id
        var tracksIdList:MutableList<Int> = mutableListOf()
        val items: List<PlaylistTrackEntity?> = playlistTrackTable.getItemsByPlaylistId(plId)
        if (items!=null) {
            items.forEach {
                tracksIdList.add((it!!.trackId))
            }
            val tracks = tracksTable.getTracksByIds(tracksIdList)
            emit(convertFromEntityToTrack(tracks))
        }
        else emit(mutableListOf())

    }
    override  fun addTrackToPlaylist(track:Track, playlist: Playlist){
        runBlocking {
            val plId = playlistsTable.getPlaylistById(playlist.id)
            if (plId!=null){
                playlistTrackTable.insertPlaylistTrack(
                    PlaylistTrackEntity(
                        playlistId=plId.playlistId,
                        trackId=track.trackId
                    )
                )
                tracksTable.insertTrack(tracksDBConverter.map(track))
                playlistsTable.incrementTracksCount(playlist.id)
            }
        }
    }
    private fun convertFromEntityToPlaylist(playlists: List<PlaylistEntity?>): List<Playlist> {
        return playlists.map { playlist -> playlistsDbConvertor.map(playlist) }
    }
    private fun convertFromPlaylistToEntity(playlists: List<Playlist?>): List<PlaylistEntity> {
        return playlists.map { playlist -> playlistsDbConvertor.map(playlist) }
    }

    private fun convertFromEntityToTrack(tracks: List<TrackEntity?>): List<Track> {
        return tracks.map { track -> tracksDBConverter.map(track) }
    }

    override  fun isTrackInPlaylist(trackId: Int, playlistId: Int): Boolean {
        val item: Int
        runBlocking{
            item = playlistTrackTable.getItemByPlaylistIdAndTrackId(trackId,playlistId)?.playlistId?:-1
        }
        if (item==-1) return false
        else return true
    }



}