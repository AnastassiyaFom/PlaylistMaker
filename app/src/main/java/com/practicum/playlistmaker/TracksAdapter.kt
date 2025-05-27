package com.practicum.playlistmaker




import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class TracksAdapter (
    private val tracks: List<Track>
) : RecyclerView.Adapter<TracksViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {

        return TracksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.found_track_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size
}



