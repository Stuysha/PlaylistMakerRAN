package com.example.sprint8.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,

    val artistName: String?,
    val artworkUrl100: String?,
    val trackName: String?,
    val trackTime: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val addTime: Long?
)