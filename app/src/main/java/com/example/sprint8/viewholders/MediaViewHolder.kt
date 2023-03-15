package com.example.sprint8.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.R
import com.example.sprint8.models.Track


class MediaViewHolder(parentView: View) : ViewHolder(parentView) {

    fun bind(model: Track) {
        itemView.findViewById<TextView>(R.id.title).text = model.trackName
        itemView.findViewById<TextView>(R.id.author).text = model.artistName
        itemView.findViewById<TextView>(R.id.time).text = model.trackTime

        val round = itemView.resources.getDimensionPixelSize(R.dimen.round_image_search)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(round))
            .into(itemView.findViewById(R.id.image))
    }
}