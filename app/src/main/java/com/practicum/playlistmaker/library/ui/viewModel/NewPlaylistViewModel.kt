package com.practicum.playlistmaker.library.ui.viewModel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsInteractor
import java.io.File
import java.io.FileOutputStream

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val context: Context
): ViewModel() {


    fun addPlaylistToBD(playlist: Playlist) {

        playlistsInteractor.addNewPlaylist(playlist)
    }

    fun saveImageToPrivateStorage(uri: Uri, albumName:String) {
        val contentResolver = context.contentResolver
        // передаём необходимый флаг на чтение
        val readFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, readFlags)

        // передаём необходимый флаг на запись
        val writeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, writeFlags)

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath: File = File(
            context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
            "playlistCover"
        )

        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, albumName + "_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }


}

