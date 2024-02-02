package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatingNewPlaylistViewModel (
    val creatingNewPlaylistInteractor : CreatingNewPlaylistInteractor
) : ViewModel (){
     fun insertNewPlaylist(name: String, description : String?, picture: String?) {
         viewModelScope.launch(Dispatchers.IO) { creatingNewPlaylistInteractor.insertNewPlaylist(
             listOf(NewPlaylistEntity())
         ) }

    }
}