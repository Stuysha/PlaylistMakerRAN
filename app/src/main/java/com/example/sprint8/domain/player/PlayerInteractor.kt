package com.example.sprint8.domain.player

import com.example.sprint8.data.converters.TrackConverter
import com.example.sprint8.data.db.entity.TrackEntity
import com.example.sprint8.domain.interfaces.PlayerRepositoryInterface
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractor(
    private val playerRepository: PlayerRepositoryInterface,
    private val movieDbConvertor: TrackConverter,
) : PlayerInteractorInterface {

    override fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    ) = playerRepository.preparePlayer(
        url, setOnPreparedListener, setOnCompletionListener
    )

    override suspend fun addFavoriteTrack(track: Track, time: Long) {
        playerRepository.addFavoriteTrack(movieDbConvertor.map(track, time))
    }

    override suspend fun removeFavoriteTrack(track: Track) {
        playerRepository.removeFavoriteTrack(movieDbConvertor.map(track, null))
    }

    override suspend fun getFavoriteTrack(idTrack: Long): Flow<TrackEntity?> {
        return playerRepository.getFavoriteTrack(idTrack)
    }

    override fun startPlayer() = playerRepository.startPlayer()
    override fun pausePlayer() = playerRepository.pausePlayer()
    override fun mediaPlayerRelease() = playerRepository.mediaPlayerRelease()
}

interface PlayerInteractorInterface {
    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    )

    suspend fun addFavoriteTrack(track: Track, time: Long)

    suspend fun removeFavoriteTrack(track: Track)

    suspend fun getFavoriteTrack(idTrack: Long): Flow<TrackEntity?>

    fun startPlayer()
    fun pausePlayer()
    fun mediaPlayerRelease()
}