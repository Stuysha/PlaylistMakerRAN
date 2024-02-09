package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import com.example.sprint8.domain.models.NewPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class CreatingNewPlaylistViewModel(
    private var idPlaylist: Long? = null,
    private val creatingNewPlaylistInteractor: CreatingNewPlaylistInteractorInterface
) : ViewModel() {
    val statePlayList = MutableLiveData<NewPlaylist>()

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

    suspend fun saveImageToPrivateStorage(absolutePath: String?, inputStream: InputStream?): File? {
        return creatingNewPlaylistInteractor.saveImageToPrivateStorage(
            absolutePath ?: return null,
            inputStream ?: return null
        )
    }
}