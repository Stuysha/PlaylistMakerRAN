package com.example.sprint8.data.search

import com.example.sprint8.data.dto.TunesResult
import com.example.sprint8.data.internet.Api
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(
    private val api: Api,
    private val historyControl: HistoryControl
) {
    fun loadSearch(
        searchText: String,
        onSuccess: (TunesResult?) -> Unit,
        onFailure: (Throwable?, String?) -> Unit
    ) {
        api.search(searchText).enqueue(
            object : Callback<TunesResult> {

                override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result == null || result.results.isNullOrEmpty()) {
                            onSuccess(null)
                        } else {
                            onSuccess(result)

                        }
                    } else {
                        onFailure(null, response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                    onFailure(t, null)
                    t.printStackTrace()

                }
            })
    }

    fun getHistory() = historyControl.getHistori()
    fun historyDelete() = historyControl.historyDelete()
    fun addHistoryTrack(track: Track) = historyControl.addTrack(track)

}