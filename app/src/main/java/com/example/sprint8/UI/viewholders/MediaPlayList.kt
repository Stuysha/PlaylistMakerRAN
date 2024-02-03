package com.example.sprint8.UI.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.R
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import java.io.File


class MediaPlayList(parentView: View) : ViewHolder(parentView) {
        fun bind(model: NewPlaylistEntity) {
        itemView.findViewById<TextView>(R.id.title).text = model.name
        itemView.findViewById<TextView>(R.id.size_tracks).text = "0 Tracks"


        val round = itemView.resources.getDimensionPixelSize(R.dimen.round_image_search)

            model.picture?.let {  Glide.with(itemView)
                .load(File(it))
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(round))
                .into(itemView.findViewById(R.id.image)) }
    }
}