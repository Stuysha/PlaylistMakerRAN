package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import com.example.sprint8.domain.models.NewPlaylist
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val idPlaylist: Long,
    private val newPlaylistInteractor: CreatingNewPlaylistInteractorInterface,
) : ViewModel() {

    var statePlayList = MutableLiveData<Pair<NewPlaylist, String>>()
    var stateTracks = MutableLiveData<List<Track>>()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val (playList, tracks, time) = newPlaylistInteractor.getPlaylistAndTracks(idPlaylist)
            statePlayList.postValue(playList to time)
            stateTracks.postValue(tracks)
        }
    }

    fun deleteTrackFromPlaylist(idTrack: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            newPlaylistInteractor.deleteTrackFromPlaylist(idPlaylist, idTrack)
            loadData()
        }
    }
}