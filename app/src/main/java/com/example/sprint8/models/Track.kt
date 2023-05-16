package com.example.sprint8.models

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId: Long,
    val collectionName: String,// название альбома
    val releaseDate: String, // год релиза
    val primaryGenreName: String, // жанр трека
    val country: String, // страна
)