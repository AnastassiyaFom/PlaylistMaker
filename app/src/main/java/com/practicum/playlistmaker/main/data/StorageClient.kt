package com.practicum.playlistmaker.main.data

interface StorageClient <T>{
        fun storeData(data: T)
        fun getData(): T?
        fun clearStorage()

}