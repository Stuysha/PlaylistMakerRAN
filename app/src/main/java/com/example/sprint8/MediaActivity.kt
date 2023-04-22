package com.example.sprint8

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.SearchActivity.Companion.TRACK
import com.example.sprint8.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class MediaActivity : AppCompatActivity() {
    var cover: ImageView? = null
    var trackName: TextView? = null
    var artistName: TextView? = null
    var timeTrack: TextView? = null
    var collectionName: TextView? = null
    var releaseDate: TextView? = null
    var primaryGenreName: TextView? = null
    var country: TextView? = null
    var trackTimeMills: TextView? = null
    var addInPlaylist: ImageView? = null
    var playback: ImageView? = null
    var likeTrack: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.arrow)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        cover = findViewById<ImageView>(R.id.imageCover)
        trackName = findViewById<TextView>(R.id.trackName)
        artistName = findViewById<TextView>(R.id.artistName)
        timeTrack = findViewById<TextView>(R.id.timeTrack)
        collectionName = findViewById<TextView>(R.id.collectionName)
        releaseDate = findViewById<TextView>(R.id.releaseDate)
        primaryGenreName = findViewById<TextView>(R.id.primaryGenreName)
        country = findViewById<TextView>(R.id.country)
        trackTimeMills = findViewById<TextView>(R.id.trackTimeMills)
        addInPlaylist = findViewById<ImageView>(R.id.addInPlaylist)
        playback = findViewById<ImageView>(R.id.playback)
        likeTrack = findViewById<ImageView>(R.id.likeTrack)

        val trackJson = intent.getStringExtra(TRACK)
        val track = Gson().fromJson<Track>(trackJson, Track::class.java)
        trackName?.setText(track.trackName)
        artistName?.setText(track.artistName)
        collectionName?.setText(track.collectionName)

        primaryGenreName?.setText(track.primaryGenreName)
        country?.setText(track.country)
        trackTimeMills?.setText(track.trackTime)
        timeTrack?.setText("00:00")

        val round = this.resources.getDimensionPixelSize(R.dimen.round_image_media8)

        Glide.with(this)
            .load(track.artworkUrl100.replace("100x100", "512x512"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(round))
            .into(cover!!)


        val date = SimpleDateFormat(
            "yyyy-MM-dd'T'hh:mm:ss'Z'",
            Locale.getDefault()
        ).parse(track?.releaseDate)

        val year = SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(date)
        releaseDate?.setText(year)

    }
}