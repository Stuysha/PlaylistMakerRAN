package com.example.sprint8.data.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TracksAndListIdEntity
import com.example.sprint8.domain.interfaces.CreatingNewPlaylistRepositoryInterface
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreatingNewPlaylistRepository(
    private val appDatabase: AppDatabase,
) : CreatingNewPlaylistRepositoryInterface {
    override suspend fun insertNewPlaylist(name: String, description: String?, picture: String?) {
        appDatabase.newPlayListDao().insertNewPlaylist(
            listOf(NewPlaylistEntity(name = name, description = description, picture = picture))
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
                TracksAndListIdEntity(idPlayList = idPlayList, idTrack = idTrack)
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
}