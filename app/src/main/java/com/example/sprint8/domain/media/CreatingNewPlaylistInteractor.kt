package com.example.sprint8.domain.media

import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.media.CreatingNewPlaylistRepositoryInterface

class CreatingNewPlaylistInteractor (val creatingNewPlaylistRepository : CreatingNewPlaylistRepositoryInterface): CreatingNewPlaylistInteractorInterface  {
    override suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        creatingNewPlaylistRepository.insertNewPlaylist(newPlaylist)
    }
    override suspend fun getNewPlaylist(): List<NewPlaylistEntity> {
        return creatingNewPlaylistRepository.getNewPlaylist()

    }
}
interface CreatingNewPlaylistInteractorInterface {
     suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

     suspend fun getNewPlaylist(): List<NewPlaylistEntity>

}