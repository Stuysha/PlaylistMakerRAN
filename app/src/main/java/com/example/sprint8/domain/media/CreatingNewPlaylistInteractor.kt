package com.example.sprint8.domain.media

import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TracksAndListId
import com.example.sprint8.data.media.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.models.NewPlaylist
import java.io.File
import java.io.InputStream

class CreatingNewPlaylistInteractor(
    val creatingNewPlaylistRepository: CreatingNewPlaylistRepositoryInterface
) : CreatingNewPlaylistInteractorInterface {
    override suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        creatingNewPlaylistRepository.insertNewPlaylist(newPlaylist)
    }

    override suspend fun getNewPlaylist(): List<NewPlaylist> {
        return creatingNewPlaylistRepository.getNewPlaylist().map {
            NewPlaylist(
                id = it.first.id,
                name = it.first.name,
                description = it.first.description,
                picture = it.first.picture,
                trackSize = it.second
            )
        }

    }

    override suspend fun insertTracksAndListId(newPlaylist: TracksAndListId): Boolean {
        return creatingNewPlaylistRepository.insertTracksAndListId(newPlaylist)
    }

    override suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File {
        return creatingNewPlaylistRepository.saveImageToPrivateStorage(basePath, inputStream)
    }
}

interface CreatingNewPlaylistInteractorInterface {
    suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

    suspend fun getNewPlaylist(): List<NewPlaylist>

    suspend fun insertTracksAndListId(newPlaylist: TracksAndListId): Boolean
    suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File
}