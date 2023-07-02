package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SearchViewModel : ViewModel() {
    private var stateLiveData = MutableLiveData(StateSearchVeiw(null, StateVeiw.IN_PROGRESS))

    fun getStateLiveData(): LiveData<StateSearchVeiw> = stateLiveData

    fun loadSearch(searchText: String) {
        // setStatusProgressBar()
        stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.IN_PROGRESS)
        RestProvider().api.search(searchText).enqueue(
            object : Callback<TunesResult> {

                override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result == null || result.results.isNullOrEmpty()) {
                           // setStatusNoContent()
                            stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_CONTENT)
                        } else {
                            //setStatusMediaList()
                            val tracks = convertToTracks(result)
                            //adapter.setItems(tracks)
                            stateLiveData.value = stateLiveData.value
                                ?.copy(
                                    listTrack = tracks,
                                    stateVeiw = StateVeiw.SHOW_CONTENT)
                        }
                    } else {
                        //setStatusNoInternet()
                        stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_INTERNET)
                        val errorJson = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                    t.printStackTrace()
                   // setStatusNoInternet()
                    stateLiveData.value = stateLiveData.value?.copy(stateVeiw = StateVeiw.NO_INTERNET)
                }
            })
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
    SHOW_HISTORY
}


