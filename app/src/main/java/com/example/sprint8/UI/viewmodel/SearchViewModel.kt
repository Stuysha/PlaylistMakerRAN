package com.example.sprint8.UI.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel : ViewModel() {
    private var stateLiveData = MutableLiveData(StateSearchVeiw(null, StateVeiw.EMPTY_VIEW))
    fun getStateLiveData(): LiveData<StateSearchVeiw> = stateLiveData
    val historyControl = HistoryControl()

    fun loadSearch(searchText: String) {
        stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.IN_PROGRESS)
        RestProvider().api.search(searchText).enqueue(
            object : Callback<TunesResult> {

                override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result == null || result.results.isNullOrEmpty()) {
                            stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_CONTENT)
                        } else {
                            val tracks = convertToTracks(result)
                            stateLiveData.value = stateLiveData.value
                                ?.copy(
                                    listTrack = tracks,
                                    stateVeiw = StateVeiw.SHOW_CONTENT)
                        }
                    } else {
                        stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_INTERNET)
                        val errorJson = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                    t.printStackTrace()
                    stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_INTERNET)
                }
            })
    }

    fun setHistory(context: Context) {
        val hihistory = historyControl.getHistori(context)
        if (!hihistory.isNullOrEmpty()) {
            stateLiveData.value = stateLiveData.value
                ?.copy(
                    listTrack = hihistory.toList(),
                    stateVeiw = StateVeiw.SHOW_HISTORY)
        }
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


