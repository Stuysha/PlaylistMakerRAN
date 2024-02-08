package com.example.sprint8.UI.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.viewholders.MediaViewHolder
import com.example.sprint8.domain.models.Track

class SearchMediaAdapter(
    private var tracks: List<Track> = listOf(),
    val useArt60: Boolean = false
) : RecyclerView.Adapter<MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_view_holder, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(tracks[position], useArt60)
        holder.itemView.setOnClickListener {
            click?.invoke(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            longClick?.invoke(tracks[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    var click: ((Track) -> Unit)? = null
    var longClick: ((Track) -> Unit)? = null
}