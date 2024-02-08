package com.example.sprint8.UI.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import com.example.sprint8.domain.models.NewPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CreatingNewPlaylistViewModel(
    private var idPlaylist: Long? = null,
    private val creatingNewPlaylistInteractor: CreatingNewPlaylistInteractorInterface
) : ViewModel() {
    var statePlayList = MutableLiveData<NewPlaylist>()

    init {
        idPlaylist?.let { id ->
            if (id == Long.MIN_VALUE){
                idPlaylist = null
                return@let
            }
            viewModelScope.launch(Dispatchers.IO) {
                val playList = creatingNewPlaylistInteractor.getPlaylist(id)
                statePlayList.postValue(playList)
            }
        }
    }

    fun insertNewPlaylist(name: String, description: String?, picture: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            creatingNewPlaylistInteractor.insertNewPlaylist(
                idPlaylist, name, description, picture
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