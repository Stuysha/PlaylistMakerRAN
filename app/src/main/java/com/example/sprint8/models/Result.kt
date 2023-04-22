package com.example.sprint8.models


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("artistName")
    val artistName: String?,
    @SerializedName("trackId")
    val trackId: Long?,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Int?,

    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String?,
    @SerializedName("country")
    val country: String?,

)