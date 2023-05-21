package com.example.sprint8

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        preparePlayer(track.previewUrl)

        playback?.setOnClickListener {

            playbackControl()
        }

    }

    override fun onPause() {

        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    var playerState = STATE_DEFAULT

    var mediaPlayer = android.media.MediaPlayer()
    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playback?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            pause()
            playerState = STATE_PREPARED
            handler.removeCallbacks(searchRunnable)
        }
    }

    fun startPlayer() {
        seconds=0
        searchDebounce()
        mediaPlayer.start()
        pause()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        handler.removeCallbacks(searchRunnable)
        mediaPlayer.pause()
        play()
        playerState = STATE_PAUSED
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun play() {
        playback?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.play
            )
        )
    }

    fun pause() {
        playback?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.pause
            )
        )
    }

    var seconds = 0
    private val searchRunnable = object : Runnable {
        override fun run() {
            seconds += 1
            timeTrack?.text = String.format("%d:%02d", seconds / 60, seconds % 60)
            handler.postDelayed(this, 1000L)
        }
    }
    private val handler = Handler(Looper.getMainLooper())

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, 1000L)
    }
}