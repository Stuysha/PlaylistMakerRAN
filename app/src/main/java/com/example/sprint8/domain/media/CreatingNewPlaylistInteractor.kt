package com.example.sprint8.domain.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TracksAndListId
import com.example.sprint8.data.media.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.models.NewPlaylist
import java.io.File
import java.io.FileOutputStream

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

    override suspend fun saveImageToPrivateStorage(uRi: Uri, context: Context?): File {
        val filePath = File(context?.filesDir, "myalbum")
        if (!filePath.exists()) {
            val result = filePath.mkdirs()

        }
        val uniqueName = "Name_${System.currentTimeMillis()}"
        val file = File(filePath, "${uniqueName}.jpg")
        val inputStream = context?.contentResolver?.openInputStream(uRi)

        val outputStream = FileOutputStream(file)

        val result = BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file
    }
}

interface CreatingNewPlaylistInteractorInterface {
    suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

    suspend fun getNewPlaylist(): List<NewPlaylist>

    suspend fun insertTracksAndListId(newPlaylist: TracksAndListId): Boolean
    suspend fun saveImageToPrivateStorage(uRi: Uri, context: Context?): File
}