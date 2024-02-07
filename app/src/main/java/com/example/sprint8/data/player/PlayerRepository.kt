package com.example.sprint8.data.player

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.FavoritesTracksEntity
import com.example.sprint8.data.db.entity.TrackEntity
import com.example.sprint8.domain.interfaces.PlayerRepositoryInterface
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
        appDatabase.favoritesTracksDao().insertFavoritesTracks(FavoritesTracksEntity(track.trackId))
        appDatabase.trackDao().insertTrack(track)
    }

    override suspend fun removeFavoriteTrack(track: TrackEntity) {
        appDatabase.favoritesTracksDao().deleteFavoritesTracks(FavoritesTracksEntity(track.trackId))
        val trackPlayList = appDatabase.newPlayListDao().getTracksAndListIdByTrack(track.trackId)
        if (trackPlayList.isEmpty()) appDatabase.trackDao().deleteTrack(track)
    }

    override suspend fun getFavoriteTrack(idTrack: Long) = flow {
        if (appDatabase.favoritesTracksDao().getFavoritesTracks(idTrack).isEmpty())
            emit(null)
        else
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