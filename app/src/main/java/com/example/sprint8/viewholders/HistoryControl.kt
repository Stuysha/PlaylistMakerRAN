package com.example.sprint8.viewholders

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.sprint8.models.Track
import com.google.gson.Gson

object HistoryControl {

    fun getHistori (context: Context) : Array<Track> {
        val sharedPreferences = context.getSharedPreferences("HISTORI_SONG", MODE_PRIVATE)
        val historyTrack  = sharedPreferences.getString("key_history", null)
        val historyTrackList = try {
            createFactsListFromJson(historyTrack!!)
        }catch (e: Exception){
            arrayOf()
        }
        return historyTrackList
    }

    fun createJsonFromFactsList(track: Array<Track>): String {
        return Gson().toJson(track)
    }
    fun addTrack (track: Track, context: Context) {
        val sharedPreferences = context.getSharedPreferences("HISTORI_SONG", MODE_PRIVATE)
        val historyTrack  = sharedPreferences.getString("key_history", null)
        val historyTrackList = try {
            createFactsListFromJson(historyTrack!!).toMutableList()
        }catch (e: Exception){
            mutableListOf()
        }

        for(item in historyTrackList){
            if (item.trackId == track.trackId) {
                historyTrackList.remove(item)
                break
            }
        }

        historyTrackList.add(0,track)


        if (historyTrackList.count()>10) {
            historyTrackList.remove(historyTrackList.last())
        }
        val historyTracks = createJsonFromFactsList(historyTrackList.toTypedArray())
         sharedPreferences.edit()
             .putString("key_history", historyTracks)
             .apply()
    }
     fun createFactsListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

}
