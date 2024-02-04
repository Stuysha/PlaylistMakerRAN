package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import com.example.sprint8.domain.models.NewPlaylist
import kotlinx.coroutines.launch

class PlaylistsViewModel(val creatingNewPlaylistInteractor : CreatingNewPlaylistInteractorInterface) : ViewModel() {
     var stateLiveData = MutableLiveData<List<NewPlaylist> >()
      fun getNewPlaylist() {
          viewModelScope.launch {  creatingNewPlaylistInteractor.getNewPlaylist().let { stateLiveData.value=it } }


    }

}