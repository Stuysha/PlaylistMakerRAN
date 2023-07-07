package com.example.sprint8.UI.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sprint8.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class MediaViewModel(private val track: Track) : ViewModel() {

    private var stateLiveData = MutableLiveData(
        StateMediaView(
            StateMediaPlayer.STATE_DEFAULT
        )
    )

    private var mediaPlayer = android.media.MediaPlayer()

    fun getStateLiveData(): LiveData<StateMediaView> = stateLiveData

    private var timeTrack = MutableLiveData("00:00")
    fun getTimeTrack(): LiveData<String> = timeTrack

    private var staticContentMedia = MutableLiveData<StaticContentMediaView>()
    fun getstaticContentMedia(): LiveData<StaticContentMediaView> = staticContentMedia


    init {
        stateLiveData.value = StateMediaView(
            urlTrack = track.previewUrl,
            playerState = StateMediaPlayer.STATE_DEFAULT
        )

        val date = SimpleDateFormat(
            "yyyy-MM-dd'T'hh:mm:ss'Z'",
            Locale.getDefault()
        ).parse(track.releaseDate)
        staticContentMedia.value = StaticContentMediaView(
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTime = track.trackTime,
            artTrack = track.artworkUrl100.replace("100x100", "512x512"),
            dateTrack = date?.let {
                SimpleDateFormat(
                    "yyyy",
                    Locale.getDefault()
                ).format(it)
            } ?: ""
        )
        preparePlayer(track.previewUrl)
    }

    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            changeState(StateMediaPlayer.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            changeState(StateMediaPlayer.STATE_PREPARED)
            stopTimer()
            resetTime()
        }
    }

    fun playbackControl() {
        when (stateLiveData.value?.playerState) {
            StateMediaPlayer.STATE_PLAYING -> {
                pausePlayer()
            }
            StateMediaPlayer.STATE_PREPARED, StateMediaPlayer.STATE_PAUSED -> {
                changeState(StateMediaPlayer.STATE_PLAYING)
                startPlayer()
            }
            null -> {}
            StateMediaPlayer.STATE_DEFAULT -> {}
        }
    }


    fun changeState(statePlayer: StateMediaPlayer) {
        if (stateLiveData.value?.playerState != statePlayer)
            stateLiveData.value =
                stateLiveData.value?.copy(playerState = statePlayer)
    }

    var seconds = 0
    private val searchRunnable = object : Runnable {
        override fun run() {
            seconds += 1
            setTime()
            handler.postDelayed(this, 1000L)
        }
    }

    fun resetTime() {
        if (stateLiveData.value?.playerState != StateMediaPlayer.STATE_PAUSED) {
            seconds = 0
            setTime()
        }
    }

    fun setTime() {
        timeTrack.value = String.format("%02d:%02d", seconds / 60, seconds % 60)
    }

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, 1000L)
    }

    fun stopTimer() {
        handler.removeCallbacks(searchRunnable)
    }

    fun startPlayer() {
        searchDebounce()
        mediaPlayer.start()
    }

    fun pausePlayer() {
        changeState(StateMediaPlayer.STATE_PAUSED)
        stopTimer()
        mediaPlayer.pause()
    }

    fun mediaPlayerRelease() {
        resetTime()
        mediaPlayer.release()
    }

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaViewModel(
                    track,
                )
            }
        }
    }
}

data class StateMediaView(
    val playerState: StateMediaPlayer,
    val urlTrack: String? = null,

    )

enum class StateMediaPlayer {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}

data class StaticContentMediaView(
    val trackName: String? = null,
    val artistName: String? = null,
    val collectionName: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val trackTime: String? = null,
    val artTrack: String? = null,
    val dateTrack: String? = null,
)