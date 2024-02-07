package com.example.sprint8.domain.interfaces

import com.example.sprint8.data.db.entity.NewPlaylistEntity
import java.io.File
import java.io.InputStream

interface CreatingNewPlaylistRepositoryInterface {

    suspend fun insertNewPlaylist(name: String, description: String?, picture: String?)
    suspend fun deleteNewPlaylist(playlistIds: List<Long>)
    suspend fun getNewPlaylist(): MutableList<Pair<NewPlaylistEntity, Int>>
    suspend fun insertTracksAndListId(idPlayList: Long, idTrack: Long): Boolean
    suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File
}