package com.example.sprint8.UI.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CreatingNewPlaylistViewModel(
    val creatingNewPlaylistInteractor: CreatingNewPlaylistInteractorInterface
) : ViewModel() {
    fun insertNewPlaylist(name: String, description: String?, picture: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            creatingNewPlaylistInteractor.insertNewPlaylist(
                name, description, picture
            )
        }

    }

    suspend fun saveImageToPrivateStorage(uRi: Uri, context: Context?): File? {
        return creatingNewPlaylistInteractor.saveImageToPrivateStorage(
            context?.filesDir?.absolutePath ?: return null,
            context.contentResolver?.openInputStream(uRi) ?: return null
        )
    }
}