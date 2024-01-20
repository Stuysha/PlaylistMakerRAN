package com.example.sprint8.data.dto


import com.example.sprint8.domain.models.TrackResult
import com.google.gson.annotations.SerializedName

data class TunesResult(
    @SerializedName("resultCount")
    val resultCount: Int?,
    @SerializedName("results")
    val results: List<TrackResult>?
)