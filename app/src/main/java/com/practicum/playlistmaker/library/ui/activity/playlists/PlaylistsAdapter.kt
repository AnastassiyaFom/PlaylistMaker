package com.practicum.playlistmaker.library.ui.activity.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.library.domain.Playlist


interface OnPlaylistItemClickListener {
    fun onItemClick(position: Int)
}

class PlaylistsAdapter (
    private val playlists: List<Playlist>,
    private val listener: OnPlaylistItemClickListener

) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount() = playlists.size
}
