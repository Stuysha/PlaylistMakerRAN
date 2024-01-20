package com.example.sprint8.data.player

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepository(
    private val mediaPlayer: android.media.MediaPlayer,
    private val appDatabase: AppDatabase,
) : PlayerRepositoryInterface {

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

    override suspend fun addFavoriteTrack(track: TrackEntity) {
        appDatabase.trackDao().insertTrack(track)
    }

    override suspend fun removeFavoriteTrack(track: TrackEntity) {
        appDatabase.trackDao().deleteTrack(track)
    }

    override suspend fun getFavoriteTrack(idTrack: Long) = flow {
        this.emit(appDatabase.trackDao().getTrack(idTrack))
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

interface PlayerRepositoryInterface {
    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    )

    suspend fun addFavoriteTrack(track: TrackEntity)

    suspend fun removeFavoriteTrack(track: TrackEntity)

    suspend fun getFavoriteTrack(idTrack: Long): Flow<TrackEntity?>

    fun startPlayer()

    fun pausePlayer()

    fun mediaPlayerRelease()
}