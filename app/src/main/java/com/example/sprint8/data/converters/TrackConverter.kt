package com.example.sprint8.data.converters

import com.example.sprint8.data.db.entity.TrackEntity
import com.example.sprint8.domain.models.Track
import com.example.sprint8.domain.models.TrackResult
import java.text.SimpleDateFormat
import java.util.Locale

class  TrackConverter {

    fun map(track: TrackResult): TrackEntity {
        return TrackEntity(
            artistName = track.artistName,
            trackId = track.trackId,
            artworkUrl100 = track.artworkUrl100,
            trackName = track.trackName,
            trackTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis),
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            addTime = null
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.trackId ?: 0L,
            trackName = track.trackName ?: "",
            artistName = track.artistName ?: "",
            trackTime = track.trackTime ?: "",
            artworkUrl100 = track.artworkUrl100 ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: "",
        )
    }

    fun map(track: Track, time: Long?): TrackEntity {
        return TrackEntity(
            trackId = track.trackId ?: 0L,
            trackName = track.trackName ?: "",
            artistName = track.artistName ?: "",
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100 ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: "",
            addTime = time
        )
    }
}