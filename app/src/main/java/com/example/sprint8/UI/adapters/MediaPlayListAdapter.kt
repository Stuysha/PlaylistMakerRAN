package com.example.sprint8.UI.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.viewholders.MediaPlayList
import com.example.sprint8.domain.models.NewPlaylist

class MediaPlayListAdapter(


    private var tracks: List<NewPlaylist> = listOf()
) : RecyclerView.Adapter<MediaPlayList>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlayList {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_play_list, parent, false)
        return MediaPlayList(view)
    }

    override fun onBindViewHolder(holder: MediaPlayList, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            click?.invoke(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(tracks: List<NewPlaylist>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    var click: ((NewPlaylist) -> Unit)? = null
}