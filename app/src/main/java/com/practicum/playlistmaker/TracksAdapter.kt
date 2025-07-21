package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class TracksAdapter (
    private val tracks: List<Track>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TracksViewHolder> () {
    private val tracksInHistoryMaxLength: Int = 10
    private val tracksInHistory: MutableList<Track> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {

        return TracksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.found_track_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    override fun getItemCount() = tracks.size

}



