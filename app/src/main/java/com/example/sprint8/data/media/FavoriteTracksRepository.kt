package com.example.sprint8.data.media

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepository(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepositoryRepositoryInterface {

    override suspend fun getFavoriteTracks() = flow {
        this.emit(appDatabase.trackDao().getTracks())
    }

}

interface FavoriteTracksRepositoryRepositoryInterface {
    suspend fun getFavoriteTracks(): Flow<List<TrackEntity>>
}