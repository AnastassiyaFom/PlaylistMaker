package com.practicum.playlistmaker.player.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.library.domain.Playlist


interface OnPlaylistItemClickListener {
    fun onItemClick(position: Int)
}

class PlaylistAddingTrackAdapter (
    private val playlists: List<Playlist>,
    private val listener: OnPlaylistItemClickListener

) : RecyclerView.Adapter<PlaylistAddingTrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistAddingTrackViewHolder {
        return PlaylistAddingTrackViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistAddingTrackViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount() = playlists.size
}