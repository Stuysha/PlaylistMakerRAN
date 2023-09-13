package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sprint8.domain.models.Track
import com.example.sprint8.domain.search.SearchInteractorInterface

class SearchViewModel(
    val searchInteractor: SearchInteractorInterface
) : ViewModel() {

    private var stateLiveData = MutableLiveData(StateSearchVeiw(null, StateVeiw.EMPTY_VIEW))
    fun getStateLiveData(): LiveData<StateSearchVeiw> = stateLiveData

    fun loadSearch(searchText: String) {
        stateLiveData.value = stateLiveData.value
            ?.copy(
                stateVeiw = StateVeiw.IN_PROGRESS
            )
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


