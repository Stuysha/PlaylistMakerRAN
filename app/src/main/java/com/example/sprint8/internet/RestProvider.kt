package com.example.sprint8.internet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RestProvider {

    val url = "https://itunes.apple.com"
    lateinit var api: Api
     init{
         val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create<Api>()
    }
}