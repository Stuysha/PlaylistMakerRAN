package com.example.sprint8.data.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TracksAndListId
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreatingNewPlaylistRepository(
    private val appDatabase: AppDatabase,
) : CreatingNewPlaylistRepositoryInterface {
    override suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        appDatabase.newPlayListDao().insertNewPlaylist(newPlaylist)
    }

    override suspend fun deleteNewPlaylist(newPlaylist: List<NewPlaylistEntity>) {
        appDatabase.newPlayListDao().deleteNewPlaylist(newPlaylist)
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

    override suspend fun insertTracksAndListId(newPlaylist: TracksAndListId): Boolean {
        val result = appDatabase.newPlayListDao()
            .getTracksAndListId(newPlaylist.idPlayList, newPlaylist.idTrack)
        return if (result.isEmpty()) {
            appDatabase.newPlayListDao().insertTracksAndListId(newPlaylist)
            true
        } else {
            false
        }
    }

    override suspend fun saveImageToPrivateStorage(
//        uRi: Uri,
        basePath: String/*context: Context?*/,
        inputStream: InputStream
    ): File {
//        val filePath = File(context?.filesDir, "myalbum")
        val filePath = File(basePath, "myalbum")
        if (!filePath.exists()) {
            val result = filePath.mkdirs()

        }
        val uniqueName = "Name_${System.currentTimeMillis()}"
        val file = File(filePath, "${uniqueName}.jpg")

//        val inputStream = context?.contentResolver?.openInputStream(uRi)

        val outputStream = FileOutputStream(file)

        val result = BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file
    }
}

interface CreatingNewPlaylistRepositoryInterface {

    suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)
    suspend fun deleteNewPlaylist(newPlaylist: List<NewPlaylistEntity>)
    suspend fun getNewPlaylist(): MutableList<Pair<NewPlaylistEntity, Int>>
    suspend fun insertTracksAndListId(newPlaylist: TracksAndListId): Boolean
    suspend fun saveImageToPrivateStorage(basePath: String, inputStream: InputStream): File
}