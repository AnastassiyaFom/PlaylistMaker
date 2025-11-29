package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    val playlistRepository: PlaylistsRepository
):PlaylistsInteractor {
    override fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
       return playlistRepository.getTracksInPlaylist(playlistId)
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track,playlist)
    }
    override fun isTrackInPlaylist(trackId: Int, playlistId:Int):Boolean{
       return  playlistRepository.isTrackInPlaylist(trackId,playlistId)
    }

    override fun getPlaylistById(playlistId: Int): Playlist {
        return  playlistRepository.getPlaylistById(playlistId)
    }

    override fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        playlistRepository.deleteTrackFromPlaylist(trackId=trackId, playlistId=playlistId)
    }

    override fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
    }
    override  fun refrashDataInDb(playlistId:Int,playlist:Playlist){
        playlistRepository.refrashDataInDb(playlistId,playlist)
    }

}