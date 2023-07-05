package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sprint8.creator.CreatorSearchObject
import com.example.sprint8.domain.models.Track
import com.example.sprint8.domain.search.SearchInteractor
import java.util.*

class SearchViewModel(
    val searchInteractor: SearchInteractor
) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])

                    return SearchViewModel(
                        CreatorSearchObject.createSearchInteractor(application)
                    ) as T
                }
            }
    }

    private var stateLiveData = MutableLiveData(StateSearchVeiw(null, StateVeiw.EMPTY_VIEW))
    fun getStateLiveData(): LiveData<StateSearchVeiw> = stateLiveData

    fun loadSearch(searchText: String) {
        searchInteractor.getSearchTrack(
            searchText,
            {
                if (it.isNullOrEmpty()) {
                    stateLiveData.value =
                        stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_CONTENT)
                } else {
                    stateLiveData.value = stateLiveData.value
                        ?.copy(
                            listTrack = it,
                            stateVeiw = StateVeiw.SHOW_CONTENT
                        )
                }
            },
            { _, _ ->
                stateLiveData.value =
                    stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_INTERNET)
            }
        )
    }

    fun setHistory() {
        val history = searchInteractor.getHistory()
        if (history.isNotEmpty()) {
            stateLiveData.value = stateLiveData.value
                ?.copy(
                    listTrack = history.toList(),
                    stateVeiw = StateVeiw.SHOW_HISTORY
                )
        }
    }

    fun historyDelete() = searchInteractor.historyDelete()
    fun saveHistoryTrack(track: Track) = searchInteractor.addHistoryTrack(track)
}

data class StateSearchVeiw(
    val listTrack: List<Track>?,
    val stateVeiw: StateVeiw
)

enum class StateVeiw {
    IN_PROGRESS,
    NO_INTERNET,
    NO_CONTENT,
    SHOW_CONTENT,
    SHOW_HISTORY,
    EMPTY_VIEW
}


