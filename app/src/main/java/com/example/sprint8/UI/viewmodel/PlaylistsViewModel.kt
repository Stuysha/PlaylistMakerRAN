package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import kotlinx.coroutines.launch

class PlaylistsViewModel(val creatingNewPlaylistInteractor : CreatingNewPlaylistInteractorInterface) : ViewModel() {
     var stateLiveData = MutableLiveData<List<NewPlaylistEntity> >()
      fun getNewPlaylist() {
          viewModelScope.launch {  creatingNewPlaylistInteractor.getNewPlaylist().let { stateLiveData.value=it } }


    }

}