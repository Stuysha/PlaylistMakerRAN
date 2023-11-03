package com.example.sprint8.data.search

import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.internet.Api
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository(
    private val api: Api,
    private val historyControl: HistoryControl,
) : SearchRepositoryInterface {

    override fun loadSearch(searchText: String): Flow<TunesResult> = flow {
        this.emit(api.search(searchText))
    }

    override fun getHistory() = historyControl.getHistori()
    override fun historyDelete() = historyControl.historyDelete()
    override fun addHistoryTrack(track: Track) = historyControl.addTrack(track)

}


interface SearchRepositoryInterface {
    fun loadSearch(searchText: String): Flow<TunesResult>

    fun getHistory(): Array<Track>
    fun historyDelete()
    fun addHistoryTrack(track: Track)
}