package com.example.sprint8.data.player

class PlayerRepository(
    private val mediaPlayer: android.media.MediaPlayer
): PlayerRepositoryInterface {

    override fun preparePlayer(
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

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

}

interface PlayerRepositoryInterface{
    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    )

    fun startPlayer()

    fun pausePlayer()

    fun mediaPlayerRelease()
}