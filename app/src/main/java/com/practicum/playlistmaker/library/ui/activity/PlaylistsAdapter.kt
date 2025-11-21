package com.practicum.playlistmaker.library.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.library.domain.Playlist


class PlaylistsAdapter (
    private val playlists: List<Playlist>

) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount() = playlists.size
}
