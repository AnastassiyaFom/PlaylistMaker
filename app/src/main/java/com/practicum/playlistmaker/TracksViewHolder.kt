package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.SearchActivity.Companion.TRACKS_HISTORY_PREFERENCES


class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val trackNameView: TextView // Название композиции
    private  val artistNameView: TextView // Имя исполнителя
    private val trackTimeView: TextView// Продолжительность трека
    private val albumImageView: ImageView // Ссылка на изображение обложки
    private val arrowView: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.track_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackTimeView = itemView.findViewById(R.id.track_time)
        albumImageView = itemView.findViewById(R.id.album_image)
        arrowView = itemView.findViewById(R.id.to_track)

    }
    fun bind(model: Track) {

        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(2f)))
            .into(albumImageView)
    }

    private fun dpToPixel(dp: Float): Int {
        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(px).toInt()
    }

}
