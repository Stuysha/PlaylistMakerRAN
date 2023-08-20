package com.example.sprint8.data.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.sprint8.data.SharedPreferencesClient
import com.example.sprint8.domain.models.Track
import com.google.gson.Gson

class HistoryControl(val context: Context) : SharedPreferencesClient {

    override fun getHistori(): Array<Track> {
        val sharedPreferences = context.getSharedPreferences("HISTORI_SONG", MODE_PRIVATE)
        val historyTrack = sharedPreferences.getString("key_history", null)
        val historyTrackList = try {
            createFactsListFromJson(historyTrack!!)
        } catch (e: Exception) {
            arrayOf()
        }
        return historyTrackList
    }

    fun createJsonFromFactsList(track: Array<Track>): String {
        return Gson().toJson(track)
    }

    override fun addTrack(track: Track) {
        val sharedPreferences = context.getSharedPreferences("HISTORI_SONG", MODE_PRIVATE)
        val historyTrack = sharedPreferences.getString("key_history", null)
        val historyTrackList = try {
            createFactsListFromJson(historyTrack!!).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }

        for (item in historyTrackList) {
            if (item.trackId == track.trackId) {
                historyTrackList.remove(item)
                break
            }
        }

        historyTrackList.add(0, track)


        if (historyTrackList.count() > 10) {
            historyTrackList.remove(historyTrackList.last())
        }
        val historyTracks = createJsonFromFactsList(historyTrackList.toTypedArray())
        sharedPreferences.edit()
            .putString("key_history", historyTracks)
            .apply()
    }

    override fun createFactsListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun historyDelete() {
        val sharedPreferences = context.getSharedPreferences("HISTORI_SONG", MODE_PRIVATE)
        sharedPreferences.edit().remove("key_history").apply()

    }
}
