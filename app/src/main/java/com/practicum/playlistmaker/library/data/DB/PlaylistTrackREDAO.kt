package com.practicum.playlistmaker.library.data.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTrackDAO {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(re:PlaylistTrackEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    suspend fun deletePlaylistTrack(re:PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_re_table WHERE playlistId = :id")
    suspend fun getTracksByPlaylistId(id:Int): List<PlaylistTrackEntity?>

    @Query("SELECT * FROM playlist_track_re_table WHERE trackId = :id")
    suspend fun getPlaylistByTracksId(id:Int): List<PlaylistTrackEntity?>

}