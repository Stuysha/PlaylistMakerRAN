package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.models.Track
import com.example.sprint8.domain.player.PlayerInteractorInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractorInterface
) : ViewModel() {

    private var stateLiveData = MutableLiveData(
        StateMediaView(
            StateMediaPlayer.STATE_DEFAULT
        )
    )

    fun getStateLiveData(): LiveData<StateMediaView> = stateLiveData
    private var timeTrack = MutableLiveData("00:00")
    fun getTimeTrack(): LiveData<String> = timeTrack
    private var staticContentMedia = MutableLiveData<StaticContentMediaView>()
    fun getStaticContentMedia(): LiveData<StaticContentMediaView> = staticContentMedia

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
        playerInteractor.preparePlayer(
            url,
            {
                changeState(StateMediaPlayer.STATE_PREPARED)
            },
            {
                changeState(StateMediaPlayer.STATE_PREPARED)
                stopTimer()
                resetTime()
            }
        )
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


    fun resetTime() {
        if (stateLiveData.value?.playerState != StateMediaPlayer.STATE_PAUSED) {
            seconds = 0
            setTime()
        }
    }

    fun setTime() {
        val time = seconds/1000
        timeTrack.value = String.format("%02d:%02d", time / 60, time % 60)
    }


    var job: Job? = null
    fun searchDebounce() {
        job?.cancel()
        job = viewModelScope.launch {
            delay(300)
            seconds += 300
            setTime()
            searchDebounce()
        }

    }

    fun stopTimer() {
        job?.cancel()

    }

    fun startPlayer() {
        searchDebounce()
        playerInteractor.startPlayer()
    }

    fun pausePlayer() {
        changeState(StateMediaPlayer.STATE_PAUSED)
        stopTimer()
        playerInteractor.pausePlayer()
    }

    fun mediaPlayerRelease() {
        resetTime()
        playerInteractor.mediaPlayerRelease()
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