package com.example.sprint8.data.player

class PlayerRepository(
    private val mediaPlayer: android.media.MediaPlayer
) {

    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            setOnPreparedListener()
        }
        mediaPlayer.setOnCompletionListener {
            setOnCompletionListener()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

}