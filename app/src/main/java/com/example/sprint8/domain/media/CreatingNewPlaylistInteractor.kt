package com.example.sprint8.domain.media

import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.media.CreatingNewPlaylistRepositoryInterface

class CreatingNewPlaylistInteractor (val creatingNewPlaylistRepository : CreatingNewPlaylistRepositoryInterface): CreatingNewPlaylistInteractorInterface  {
    override suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        creatingNewPlaylistRepository.insertNewPlaylist(newPlaylist)
    }
}
interface CreatingNewPlaylistInteractorInterface {
     suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

}