package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.domain.FileRepository
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment.Companion.ADDING_FRAGMENT
import com.practicum.playlistmaker.library.ui.activity.playlists.PlaylistAddingFragment.Companion.PLAYLIST_SCREEN_FRAGMENT
import java.io.File
import java.io.FileOutputStream

class PlaylistAddingViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val fileRepository: FileRepository,
    private val context: Context
): ViewModel() {
    private var playlist:Playlist=Playlist()
    private var albumName=""
    private var albumDescription=""
    private var imageUri: Uri? =null
    private var imageDrawable:Drawable? = null
    private var saveButtonText = ""
    private var fragmentHadder = ""
    private var previousFragment=""
    private var playlistId=-1


    fun getSaveButtonText() = saveButtonText
    fun getFragmentHadder() = fragmentHadder
    fun getAlbumName() = albumName
    fun getAlbumDescription()=albumDescription
    fun getImageUri() = imageUri
    fun getImageDrawable() = imageDrawable
    fun setAlbumName(str: String) {
        albumName = str
    }
    fun setAlbumDescription(str: String){
        albumDescription = str
    }
    fun setImageUri(uri: Uri) {
        imageUri = uri
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setPlaylistData(prFragment:String, plListId:Int){
        playlistId = plListId
        previousFragment = prFragment
        when (previousFragment){
            PLAYLIST_SCREEN_FRAGMENT ->
            {
                saveButtonText = context.getString(R.string.save)
                fragmentHadder = context.getString(R.string.edit)
                imageDrawable = context.getDrawable(R.drawable.album_placeholder_512)
                playlist =  playlistsInteractor.getPlaylistById(playlistId)
                if (playlistId>-1) {
                    albumName = playlist.playlstName
                    albumDescription = playlist.playlistDescription ?: ""
                    imageUri = playlist.playlistImageDir ?: null
                }
            }
            else->{
                saveButtonText = context.getString(R.string.create)
                fragmentHadder = context.getString(R.string.new_playlist)
            }
        }
    }
    fun savePlaylist() {
        if (albumName.isNotEmpty()) {
            val playlist = Playlist(
                playlstName = albumName,
                playlistDescription = albumDescription,
                playlistImageDir = imageUri
            )
            // Как проверить на то, что не существует картинки с таким именем
            if (imageUri != null && imageUri.toString().isNotEmpty()) {
                saveImageToPrivateStorage(imageUri!!, albumName)
            }

            when (previousFragment) {
                PLAYLIST_SCREEN_FRAGMENT -> {
                    refrashDataInDb(playlistId,playlist)
                }
                else -> addPlaylistToBD(playlist)
            }
        }
    }
    fun hasData() = albumName.isNotEmpty()|| albumDescription.isNotEmpty()||imageUri.toString().isNotEmpty()


    private fun addPlaylistToBD(playlist: Playlist) {
        playlistsInteractor.addNewPlaylist(playlist)
    }

    private fun saveImageToPrivateStorage(uri: Uri, albumName:String) {
        fileRepository.saveImageToPrivateStorage(uri,albumName)
    }
    private fun refrashDataInDb(playlistId:Int,playlist:Playlist){
        playlistsInteractor.refrashDataInDb(playlistId,playlist)
    }

    fun getSavePlaylistMessage(): String {
        return when (previousFragment){
            PLAYLIST_SCREEN_FRAGMENT -> "Плейлист $albumName обновлен"
            else->"Плейлист $albumName создан"
        }
    }

}

