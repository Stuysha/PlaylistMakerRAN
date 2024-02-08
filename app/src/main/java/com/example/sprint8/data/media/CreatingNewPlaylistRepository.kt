package com.example.sprint8.data.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sprint8.data.converters.TrackConverter
import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TrackEntity
import com.example.sprint8.data.db.entity.TracksAndListIdEntity
import com.example.sprint8.domain.interfaces.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.models.Track
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreatingNewPlaylistRepository(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TrackConverter,
) : CreatingNewPlaylistRepositoryInterface {
    override suspend fun insertNewPlaylist(
        idPlayList: Long?,
        name: String,
        description: String?,
        picture: String?
    ) {
        appDatabase.newPlayListDao().insertNewPlaylist(
            listOf(
                NewPlaylistEntity(
                    id = idPlayList ?: 0,
                    name = name,
                    description = description,
                    picture = picture
                )
            )
        )
    }

    override suspend fun deleteNewPlaylist(playlistIds: List<Long>) {
        appDatabase.newPlayListDao()
            .deleteNewPlaylist(
                playlistIds.map { NewPlaylistEntity(id = it, null, null, null) }
            )
    }

    override suspend fun getNewPlaylist(): MutableList<Pair<NewPlaylistEntity, Int>> {
        val playlist = appDatabase.newPlayListDao().getNewPlaylist()
        val svyzi = appDatabase.newPlayListDao().getTracksAndListId()
        val result = mutableListOf<Pair<NewPlaylistEntity, Int>>()
        playlist.forEach { playlist ->
            result.add(playlist to
                    svyzi.count {
                        it.idPlayList == playlist.id
                    })
        }

        return result

    }

    override suspend fun insertTracksAndListId(idPlayList: Long, idTrack: Long): Boolean {
        val result = appDatabase.newPlayListDao()
            .getTracksAndListId(idPlayList, idTrack)
        return if (result.isEmpty()) {
            appDatabase.newPlayListDao().insertTracksAndListId(
                TracksAndListIdEntity(
                    idPlayList = idPlayList,
                    idTrack = idTrack,
                    addTime = System.currentTimeMillis()
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun saveImageToPrivateStorage(
        basePath: String,
        inputStream: InputStream
    ): File {
        val filePath = File(basePath, "myalbum")
        if (!filePath.exists()) {
            val result = filePath.mkdirs()
        }
        val uniqueName = "Name_${System.currentTimeMillis()}"
        val file = File(filePath, "${uniqueName}.jpg")

        val outputStream = FileOutputStream(file)

        val result = BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file
    }

    override suspend fun getPlaylistAndTracks(id: Long): Pair<NewPlaylistEntity, List<Track>> {
        val playList = appDatabase.newPlayListDao().getPlayList(id)
        val idTrack = appDatabase.newPlayListDao().getTracksAndListId(id).map { it.idTrack }

        val tracks = mutableListOf<TrackEntity>()
        idTrack.forEach {
            appDatabase.trackDao().getTrack(it)?.let { it1 -> tracks.add(it1) }
        }

        return playList to tracks.map { movieDbConvertor.map(it) }
    }

    override suspend fun getPlaylist(id: Long): NewPlaylistEntity {
        return appDatabase.newPlayListDao().getPlayList(id)
    }

    override suspend fun deleteTrackFromPlaylist(idPlayList: Long, idTrack: Long) {
        appDatabase.newPlayListDao().deleteTracksAndListId(idPlayList, idTrack)
        checkAndDeleteTrack(idTrack)
    }

    suspend fun checkAndDeleteTrack(idTrack: Long) {
        if (appDatabase.favoritesTracksDao().getFavoritesTracks(idTrack).isEmpty())
            if (appDatabase.newPlayListDao().getTracksAndListIdByTrack(idTrack).isEmpty())
                appDatabase.trackDao().deleteTrack(idTrack)
    }

    override suspend fun deletePlayList(idPlayList: Long) {
        val playlistDao = appDatabase.newPlayListDao()
        val idTracks = playlistDao.getTracksAndListId(idPlayList)
        playlistDao.deleteNewPlaylist(listOf(NewPlaylistEntity(id = idPlayList, null, null, null)))
        playlistDao.deleteTracksAndListId(idPlayList)
        idTracks.forEach {
            checkAndDeleteTrack(it.idTrack)
        }
    }
}