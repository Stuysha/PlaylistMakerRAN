package com.example.sprint8.UI.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.R
import com.example.sprint8.UI.activity.SearchActivity.Companion.TRACK
import com.example.sprint8.UI.viewmodel.MediaViewModel
import com.example.sprint8.UI.viewmodel.StateMediaPlayer
import com.example.sprint8.domain.models.Track
import com.google.gson.Gson
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

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
        val trackObject = Gson().fromJson(trackJson, Track::class.java)
        viewModel = getKoin().get(parameters = {parametersOf(trackObject)})
        val toolbar = findViewById<Toolbar>(R.id.arrow)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        cover = findViewById(R.id.imageCover)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        timeTrack = findViewById(R.id.timeTrack)
        collectionName = findViewById(R.id.collectionName)
        releaseDate = findViewById(R.id.releaseDate)
        primaryGenreName = findViewById(R.id.primaryGenreName)
        country = findViewById(R.id.country)
        trackTimeMills = findViewById(R.id.trackTimeMills)
        addInPlaylist = findViewById(R.id.addInPlaylist)
        playback = findViewById(R.id.playback)
        likeTrack = findViewById(R.id.likeTrack)

        timeTrack?.setText(R.string.null_time)
        playback?.isEnabled = false

        playback?.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getStateLiveData().observe(this) {
            when (it.playerState) {
                StateMediaPlayer.STATE_DEFAULT -> {
                }
                StateMediaPlayer.STATE_PREPARED -> {
                    playback?.isEnabled = true
                    setPlayView()
                }
                StateMediaPlayer.STATE_PLAYING -> startPlayer()
                StateMediaPlayer.STATE_PAUSED -> pausePlayer()
            }
        }
        viewModel.getTimeTrack().observe(this) {
            timeTrack?.text = it.orEmpty()
        }
        viewModel.getStaticContentMedia().observe(this) {
            releaseDate?.text = it.dateTrack ?: getString(R.string.is_unknown).lowercase()
            trackName?.text = it.trackName
            artistName?.text = it.artistName
            collectionName?.text = it.collectionName

            primaryGenreName?.text = it.primaryGenreName
            country?.text = it.country
            trackTimeMills?.text = it.trackTime
            val round = this.resources.getDimensionPixelSize(R.dimen.round_image_media8)

            Glide.with(this)
                .load(it.artTrack)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(round))
                .into(cover ?: return@observe)
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mediaPlayerRelease()
    }

    fun startPlayer() {
        setViewPause()
    }

    fun pausePlayer() {
        setPlayView()
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