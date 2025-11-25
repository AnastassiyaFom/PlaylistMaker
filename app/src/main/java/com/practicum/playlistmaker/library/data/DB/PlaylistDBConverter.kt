package com.practicum.playlistmaker.library.data.DB

import androidx.core.net.toUri
import com.practicum.playlistmaker.library.domain.Playlist


class PlaylistDBConverter {
    fun map(playlist: Playlist?): PlaylistEntity {

        return PlaylistEntity(
            playlstName=playlist?.playlstName?:"",
        playlistDescription=playlist?.playlistDescription?:"",
        playlistImageDir=playlist?.playlistImageDir?.toString()?:"",
        tracks=playlist?.tracks?:"",
        tracksCount=playlist?.tracksCount?:0
        )

    }

    fun map(playlist: PlaylistEntity?): Playlist{
        return Playlist(
            id = playlist?.playlistId?:-1,
            playlstName =playlist?.playlstName?:"",
            playlistDescription =playlist?.playlistDescription?:"",
            playlistImageDir =playlist?.playlistImageDir?.toUri(),
            tracks =playlist?.tracks?: "",
            tracksCount =playlist?.tracksCount?:0
        )
    }

}