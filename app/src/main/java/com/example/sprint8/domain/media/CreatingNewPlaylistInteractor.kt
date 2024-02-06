package com.example.sprint8.domain.media

import com.example.sprint8.domain.interfaces.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.models.NewPlaylist
import java.io.File
import java.io.InputStream

class CreatingNewPlaylistInteractor(
    val creatingNewPlaylistRepository: CreatingNewPlaylistRepositoryInterface
) : CreatingNewPlaylistInteractorInterface {
    override suspend fun insertNewPlaylist(name: String, description: String?, picture: String?) {
        creatingNewPlaylistRepository.insertNewPlaylist(name, description, picture)
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

    override suspend fun insertTracksAndListId(idPlayList: Long, idTrack: Long): Boolean {
        return creatingNewPlaylistRepository.insertTracksAndListId(idPlayList, idTrack)
    }

    override suspend fun saveImageToPrivateStorage(
        basePath: String,
        inputStream: InputStream
    ): File {
        return creatingNewPlaylistRepository.saveImageToPrivateStorage(basePath, inputStream)
    }
}

interface CreatingNewPlaylistInteractorInterface {
    suspend fun insertNewPlaylist(name: String, description: String?, picture: String?)

    suspend fun getNewPlaylist(): List<NewPlaylist>

    suspend fun insertTracksAndListId(idPlayList: Long, idTrack: Long): Boolean
    suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File
}