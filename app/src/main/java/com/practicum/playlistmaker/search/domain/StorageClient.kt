package com.practicum.playlistmaker.search.domain

interface StorageClient <T>{
        fun storeData(data: T)
        fun getData(): T?
        fun clearStorage()

}