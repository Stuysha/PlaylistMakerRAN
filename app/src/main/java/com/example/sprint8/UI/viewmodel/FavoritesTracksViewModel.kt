package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint8.domain.media.FavoriteTracksInteractorInterface
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesTracksViewModel(
    private val interactor: FavoriteTracksInteractorInterface
) : ViewModel() {

    var stateLiveData = MutableLiveData(FavoritesTracksView())

    init {
        updateFavoriteTracks()
    }

    fun updateFavoriteTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getFavoriteTracks().collect {
                withContext(Dispatchers.Main) {
                    stateLiveData.value = stateLiveData.value?.copy(
                        listTrack = it,
                        isLoadData = true
                    )
                }
            }
        }
    }
}

data class FavoritesTracksView(
    val listTrack: List<Track>? = null,
    val isLoadData: Boolean = false
)