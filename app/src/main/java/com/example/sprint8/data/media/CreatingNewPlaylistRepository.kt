package com.example.sprint8.data.media

import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.NewPlaylistEntity

class CreatingNewPlaylistRepository(
    private val appDatabase: AppDatabase,
) : CreatingNewPlaylistRepositoryInterface {
    override suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        appDatabase.newPlayListDao().insertNewPlaylist(newPlaylist)
    }

    override suspend fun deleteNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        appDatabase.newPlayListDao().deleteNewPlaylist(newPlaylist)
    }

    override suspend fun getNewPlaylist(): List<NewPlaylistEntity> {
       return appDatabase.newPlayListDao().getNewPlaylist()

    }

}

interface CreatingNewPlaylistRepositoryInterface {

    suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)
    suspend fun deleteNewPlaylist(newPlaylist: List<NewPlaylistEntity>)
    suspend fun getNewPlaylist(): List<NewPlaylistEntity>

}