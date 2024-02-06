package com.example.sprint8.domain.interfaces

import com.example.sprint8.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

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