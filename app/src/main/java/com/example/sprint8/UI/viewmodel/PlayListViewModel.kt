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

    private fun loadData() {
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

    fun isNotEmptyTracks() = stateTracks.value.isNullOrEmpty().not()

    fun createStringShared(): String {
        var string = "${stateTracks.value?.size} треков\n"
        stateTracks.value?.forEachIndexed { index, track ->
            string += "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})\n"
        }
        return string
    }

    suspend fun deletePlayList() {
        newPlaylistInteractor.deletePlayList(idPlaylist)
    }
}