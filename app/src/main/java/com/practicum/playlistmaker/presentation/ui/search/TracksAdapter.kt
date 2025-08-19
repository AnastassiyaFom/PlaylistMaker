package com.practicum.playlistmaker.presentation.ui.search


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class TracksAdapter (
    private val tracks: List<Track>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    override fun getItemCount() = tracks.size
}



