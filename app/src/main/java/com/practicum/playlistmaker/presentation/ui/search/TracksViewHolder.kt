package com.practicum.playlistmaker.presentation.ui.search

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FoundTrackItemBinding
import com.practicum.playlistmaker.domain.models.Track

class TracksViewHolder(private val binding: FoundTrackItemBinding) :
RecyclerView.ViewHolder(binding.root){

    fun bind(model: Track) {

        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(2f)))
            .into(binding.albumImage)
    }

    private fun dpToPixel(dp: Float): Int {
        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(px)
    }
    companion object {
        fun from(parent: ViewGroup): TracksViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FoundTrackItemBinding.inflate(inflater, parent, false)
            return TracksViewHolder(binding)
        }
    }

}
