package com.example.sprint8.domain.interfaces

import com.example.sprint8.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepositoryRepositoryInterface {
    suspend fun getFavoriteTracks(): Flow<List<TrackEntity>>
}