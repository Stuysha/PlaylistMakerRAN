package com.example.sprint8.UI.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.viewholders.PlayListItemViewHolder
import com.example.sprint8.domain.models.NewPlaylist

class PlayListAdapter(
    private var tracks: List<NewPlaylist> = listOf()
) : RecyclerView.Adapter<PlayListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.play_lists, parent, false)
        return PlayListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayListItemViewHolder, position: Int) {
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