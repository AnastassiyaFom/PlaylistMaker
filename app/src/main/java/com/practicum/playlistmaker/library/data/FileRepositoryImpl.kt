package com.practicum.playlistmaker.library.data

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.practicum.playlistmaker.library.domain.FileRepository
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl (private val context: Context): FileRepository {
    override fun saveImageToPrivateStorage(uri: Uri, albumName: String) {
        val contentResolver = context.contentResolver
        // передаём необходимый флаг на чтение
        val readFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, readFlags)

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