package com.practicum.playlistmaker.library.data.DB

import androidx.core.net.toUri
import com.practicum.playlistmaker.library.domain.Playlist


class PlaylistDBConverter {
    fun map(playlist: Playlist): PlaylistEntity {

        return PlaylistEntity(
            playlstName=playlist.playlstName,
        playlistDescription=playlist.playlistDescription,
        playlistImageDir=playlist.playlistImageDir?.toString()?:"",
        tracks=playlist.tracks.toString(),
        tracksCount=playlist.tracksCount
        )

    }

    fun map(playlist: PlaylistEntity?): Playlist{
        return Playlist(
            playlstName =playlist?.playlstName?:"",
            playlistDescription =playlist?.playlistDescription?:"",
            playlistImageDir =playlist?.playlistImageDir?.toUri(),
            tracks =playlist?.tracks?: "",
            tracksCount =playlist?.tracksCount?:0
        )
    }
}