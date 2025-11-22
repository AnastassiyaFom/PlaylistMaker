package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInPlayerItemBinding
import com.practicum.playlistmaker.library.domain.Playlist


class PlaylistAddingTrackViewHolder (private val binding: FragmentPlaylistInPlayerItemBinding,
                                     val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        binding.name.text = model.playlstName
        if (model.playlistImageDir!=null&&model.playlistImageDir.toString().isNotEmpty()) {
            binding.image.setImageURI(model.playlistImageDir)
            binding.image.setScaleType(CENTER_CROP)
        }
        else {
            binding.image.setImageDrawable( ResourcesCompat.getDrawable(context.resources,
                R.drawable.placeholder102,null ))
        }
        binding.count.text = getTrackCountString(model.tracksCount)
    }

    private fun getTrackCountString(count:Int):String {
        var trackStr=""
        when (count){
            1->trackStr = context.resources.getString(R.string.one_track)
            in 2..4->trackStr = context.resources.getString(R.string.two_tracks)
            else->trackStr = context.resources.getString(R.string.many_tracks)
        }
        trackStr = count.toString() + " " + trackStr
        return trackStr
    }

    companion object {
        fun from (parent: ViewGroup): PlaylistAddingTrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FragmentPlaylistInPlayerItemBinding.inflate(inflater, parent, false)
            return PlaylistAddingTrackViewHolder(binding, parent.context)
        }
    }


}