package com.example.sprint8.data

import com.example.sprint8.domain.models.Track

interface SharedPreferencesClient {
    fun getHistori(): Array<Track>

    fun addTrack(track: Track)

    fun createFactsListFromJson(json: String): Array<Track>

    fun historyDelete()
}