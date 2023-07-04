package com.example.sprint8.UI.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.R
import com.example.sprint8.UI.activity.SearchActivity.Companion.TRACK
import com.example.sprint8.UI.viewmodel.MediaViewModel
import com.example.sprint8.UI.viewmodel.StateMediaPlayer
import com.example.sprint8.domain.models.Track
import com.google.gson.Gson

class MediaActivity : AppCompatActivity() {
    private lateinit var viewModel: MediaViewModel
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
        val trackJson = intent.getStringExtra(TRACK)
        val trackR = Gson().fromJson<Track>(trackJson, Track::class.java)
        viewModel = ViewModelProvider(
            this,
            MediaViewModel.getViewModelFactory(trackR)
        )[MediaViewModel::class.java]
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



        timeTrack?.setText("00:00")

        playback?.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getStateLiveData().observe(this) {
            when (it.playerState) {
                StateMediaPlayer.STATE_DEFAULT -> {
                    preparePlayer(it.urlTrack ?: "")
                }
                StateMediaPlayer.STATE_PREPARED -> {}
                StateMediaPlayer.STATE_PLAYING -> startPlayer()
                StateMediaPlayer.STATE_PAUSED -> pausePlayer()
            }
        }
        viewModel.getTimeTrack().observe(this) {
            timeTrack?.text = it ?: ""
        }
        viewModel.getstaticContentMedia().observe(this){
            releaseDate?.setText(it.dateTrack ?: "")
            trackName?.setText(it.trackName)
            artistName?.setText(it.artistName)
            collectionName?.setText(it.collectionName)

            primaryGenreName?.setText(it.primaryGenreName)
            country?.setText(it.country)
            trackTimeMills?.setText(it.trackTime)
            val round = this.resources.getDimensionPixelSize(R.dimen.round_image_media8)

            Glide.with(this)
                .load(it.artTrack)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(round))
                .into(cover!!)
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

    var mediaPlayer = android.media.MediaPlayer()
    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playback?.isEnabled = true
            viewModel.changeState(StateMediaPlayer.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            setPlayView()
            viewModel.changeState(StateMediaPlayer.STATE_PREPARED)
            viewModel.stopTimer()
            viewModel.resetTime()
        }
    }

    fun startPlayer() {
        viewModel.searchDebounce()
        mediaPlayer.start()
        setViewPause()
        viewModel.changeState(StateMediaPlayer.STATE_PLAYING)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        setPlayView()
        viewModel.changeState(StateMediaPlayer.STATE_PLAYING)
    }

    fun setPlayView() {
        playback?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.play
            )
        )
    }

    fun setViewPause() {
        playback?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.pause
            )
        )
    }
}