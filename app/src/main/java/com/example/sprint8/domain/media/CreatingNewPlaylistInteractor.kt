package com.example.sprint8.domain.media

import com.example.sprint8.domain.interfaces.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.interfaces.TrackRepositoryInterface
import com.example.sprint8.domain.models.NewPlaylist
import com.example.sprint8.domain.models.Track
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

class CreatingNewPlaylistInteractor(
    private val creatingNewPlaylistRepository: CreatingNewPlaylistRepositoryInterface,
    private val trackRepository: TrackRepositoryInterface
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

    override suspend fun getPlaylistAndTracks(id: Long): Triple<NewPlaylist, List<Track>, String> {
        val it = creatingNewPlaylistRepository.getPlaylist(id)

        val summ = it.second.sumOf {
            SimpleDateFormat("mm:ss", Locale.getDefault()).parse(it.trackTime)?.time ?: 0
        }

        return Triple(
            NewPlaylist(
                id = it.first.id,
                name = it.first.name,
                description = it.first.description,
                picture = it.first.picture,
                trackSize = it.second.size
            ),
            it.second,
            SimpleDateFormat("mm", Locale.getDefault()).format(summ)
        )
    }

    override suspend fun insertTracksAndListId(idPlayList: Long, track: Track): Boolean {
        val result = creatingNewPlaylistRepository.insertTracksAndListId(idPlayList, track.trackId)
        trackRepository.insertTrack(track)
        return result
    }

    override suspend fun saveImageToPrivateStorage(
        basePath: String,
        inputStream: InputStream
    ): File {
        return creatingNewPlaylistRepository.saveImageToPrivateStorage(basePath, inputStream)
    }

    override suspend fun deleteTrackFromPlaylist(idPlayList: Long, idTrack: Long) {
        creatingNewPlaylistRepository.deleteTrackFromPlaylist(idPlayList, idTrack)
    }

    override suspend fun deletePlayList(idPlayList: Long) {
        creatingNewPlaylistRepository.deletePlayList(idPlayList)
    }
}

interface CreatingNewPlaylistInteractorInterface {
    suspend fun insertNewPlaylist(name: String, description: String?, picture: String?)

    suspend fun getNewPlaylist(): List<NewPlaylist>

    suspend fun getPlaylistAndTracks(id: Long): Triple<NewPlaylist, List<Track>, String>

    suspend fun insertTracksAndListId(idPlayList: Long, track: Track): Boolean
    suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File
    suspend fun deleteTrackFromPlaylist(idPlayList: Long, idTrack: Long)

    suspend fun deletePlayList(idPlayList: Long)
}