package com.example.sprint8.data.media

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.domain.interfaces.FavoriteTracksRepositoryRepositoryInterface
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepository(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepositoryRepositoryInterface {

    override suspend fun getFavoriteTracks() = flow {
        this.emit(appDatabase.trackDao().getTracks())
    }

}

