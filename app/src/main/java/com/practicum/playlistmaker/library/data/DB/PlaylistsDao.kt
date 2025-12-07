package com.practicum.playlistmaker.library.data.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE playlistId = :id")
    suspend fun deletePlaylistById(id: Int)

    @Query("SELECT * FROM playlists_table WHERE playlistId = :id")
    suspend fun getPlaylistById(id:Int): PlaylistEntity?

    @Query("SELECT * FROM playlists_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity?>

    @Query("UPDATE playlists_table SET tracks = :newValue WHERE playlistId = :id")
    fun updateFieldTracksById(id: Int, newValue: String)

    @Query("UPDATE playlists_table SET tracksCount = tracksCount + 1 WHERE playlistId = :playlistId")
    suspend fun incrementTracksCount(playlistId: Int)

    @Query("UPDATE playlists_table SET tracksCount = tracksCount - 1 WHERE playlistId = :playlistId")
    suspend fun decrementTracksCount(playlistId: Int)

}