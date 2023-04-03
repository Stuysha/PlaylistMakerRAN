package com.example.sprint8.models


import com.google.gson.annotations.SerializedName

data class TunesResult(
    @SerializedName("resultCount")
    val resultCount: Int?,
    @SerializedName("results")
    val results: List<Result?>?
)