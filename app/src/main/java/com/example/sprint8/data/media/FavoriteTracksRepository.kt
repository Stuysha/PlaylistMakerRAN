package com.example.sprint8.data.media

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.domain.interfaces.FavoriteTracksRepositoryInterface
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepository(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepositoryInterface {

    override suspend fun getFavoriteTracks() = flow {
        this.emit(appDatabase.trackDao().getTracks())
    }

}

