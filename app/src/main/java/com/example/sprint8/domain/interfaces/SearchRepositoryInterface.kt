package com.example.sprint8.domain.interfaces

import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepositoryInterface {
    fun loadSearch(searchText: String): Flow<TunesResult>

    fun getHistory(): Array<Track>
    fun historyDelete()
    fun addHistoryTrack(track: Track)
}