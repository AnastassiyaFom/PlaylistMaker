package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.view.OnItemClickListener
import com.practicum.playlistmaker.search.ui.view.TracksAdapter
import com.practicum.playlistmaker.search.ui.view.TracksViewHolder

fun interface OnItemLongClickListener {
    fun onItemLongClick(position: Int)
}


class TracksInPlaylistAdapter (
    private val tracks: List<Track>,
    private val listener: OnItemClickListener,
    private val longListener: OnItemLongClickListener,
) : TracksAdapter(tracks, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            longListener.onItemLongClick(position)
            true
        }
    }

    override fun getItemCount() = tracks.size
}

