package com.practicum.playlistmaker.search.data


import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.StorageClient

import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val dataKey: String,
    private val type: Type,
    private val gson:Gson,
    private val sharedPrefs: SharedPreferences
) : StorageClient<T> {

    override fun storeData(data: T) {

        sharedPrefs.edit()
            .remove(dataKey)
            .putString(dataKey, gson.toJson(data, type))
            .apply()
    }

    override fun getData(): T? {
        val dataJson: String? = sharedPrefs.getString(dataKey, "")
        if (dataJson.isNullOrEmpty()) {
            return null
        } else {
            return gson.fromJson(dataJson, type)
        }

    }

    override fun clearStorage() {
        sharedPrefs.edit()
            .remove(dataKey)
            .apply()
    }
}


