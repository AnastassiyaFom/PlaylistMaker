package com.practicum.playlistmaker.library.ui.activity.playlists


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistItemBinding
import com.practicum.playlistmaker.library.domain.Playlist


class PlaylistViewHolder (private val binding: FragmentPlaylistItemBinding,
                          val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Playlist) {
        binding.name.text = model.playlstName
        if (model.playlistImageDir!=null&&model.playlistImageDir.toString().isNotEmpty()) {
            binding.image.setImageURI(model.playlistImageDir)
            binding.image.setScaleType(CENTER_CROP)
        }
        else {
            binding.image.setImageDrawable( ResourcesCompat.getDrawable(context.resources,R.drawable.placeholder102,null ))
        }
        binding.count.text = getTrackCountString(model.tracksCount)
    }

    private fun getTrackCountString(count:Int):String {
        return count.toString()+" " + context.resources.getQuantityString(R.plurals.plural_track,count)
    }

    companion object {
        fun from (parent: ViewGroup): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FragmentPlaylistItemBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding, parent.context)
        }
    }


}