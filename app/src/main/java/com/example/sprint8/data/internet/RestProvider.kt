package com.example.sprint8.data.internet

import com.example.sprint8.data.NetworkClient
import com.example.sprint8.data.dto.Response
import com.example.sprint8.data.dto.TunesResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RestProvider : NetworkClient {

    private val url = "https://itunes.apple.com"
    val api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create<Api>()
    }


    override fun search(text: String): Response<TunesResult?> {
        val response = Response<TunesResult?>()
        try {
            val resp = api.search(text).execute()
            val body = resp.body()

            if (body != null) {
                response.resultCode = 0
                response.data = body
            } else {
                response.resultCode = 1
            }
        } catch (e: Throwable) {
            response.resultCode = 2
        }
        return response
    }
}