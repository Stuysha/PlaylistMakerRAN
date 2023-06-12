package com.example.sprint8.data

import android.content.Context
import com.example.sprint8.domain.models.Track

interface SharedPreferencesClient {
    fun getHistori(context: Context): Array<Track>

    fun addTrack(track: Track, context: Context)

    fun createFactsListFromJson(json: String): Array<Track>

    fun historyDelete(context: Context)
}