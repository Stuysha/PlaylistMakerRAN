package com.example.sprint8.domain.search

import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class SearchInteractor(val serRepository: SearchRepository) {

    fun getSearchTrack(
        searchText: String,
        onSuccess: (MutableList<Track>?) -> Unit,
        onFailure: (Throwable?, String?) -> Unit
    ) {
        serRepository.loadSearch(
            searchText,
            {
                onSuccess(it?.let { it1 -> convertToTracks(it1) })
            },
            { error, message ->
                onFailure(error, message)
            }
        )
    }

    fun convertToTracks(tunes: TunesResult): MutableList<Track> {
        val tracList = mutableListOf<Track>()
        tunes.results?.forEach {
            tracList.add(
                Track(
                    trackId = it?.trackId ?: 0L,
                    trackName = it?.trackName ?: "",
                    artistName = it?.artistName ?: "",
                    trackTime = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(it?.trackTimeMillis),
                    artworkUrl100 = it?.artworkUrl100 ?: "",
                    collectionName = it?.collectionName ?: "",
                    releaseDate = it?.releaseDate ?: "",
                    primaryGenreName = it?.primaryGenreName ?: "",
                    country = it?.country ?: "",
                    previewUrl = it?.previewUrl ?: "",
                )
            )
        }
        return tracList
    }

    fun getHistory() = serRepository.getHistory()
    fun historyDelete() = serRepository.historyDelete()
    fun addHistoryTrack(track: Track) = serRepository.addHistoryTrack(track)
}