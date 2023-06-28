package com.example.sprint8.data.internet

import com.example.sprint8.data.dto.TunesResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TunesResult>

}