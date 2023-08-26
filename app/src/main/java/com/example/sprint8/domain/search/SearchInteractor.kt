package com.example.sprint8.domain.search

import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.search.SearchRepositoryInterface
import com.example.sprint8.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchInteractor(val searchRepository: SearchRepositoryInterface): SearchInteractorInterface {

    override fun getSearchTrack(
        searchText: String,
        onSuccess: (MutableList<Track>?) -> Unit,
        onFailure: (Throwable?, String?) -> Unit
    ) {
        searchRepository.loadSearch(
            searchText,
            {
                onSuccess(it?.let { convertToTracks(it) })
            },
            { error, message ->
                onFailure(error, message)
            }
        )
    }

    private fun convertToTracks(tunes: TunesResult): MutableList<Track> {
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

    override fun getHistory() = searchRepository.getHistory()
    override fun historyDelete() = searchRepository.historyDelete()
    override fun addHistoryTrack(track: Track) = searchRepository.addHistoryTrack(track)
}

interface SearchInteractorInterface {
    fun getSearchTrack(
        searchText: String,
        onSuccess: (MutableList<Track>?) -> Unit,
        onFailure: (Throwable?, String?) -> Unit
    )

    fun getHistory(): Array<Track>
    fun historyDelete()
    fun addHistoryTrack(track: Track)
}