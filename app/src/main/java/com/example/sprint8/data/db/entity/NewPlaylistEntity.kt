package com.example.sprint8.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NewPlaylistEntity (
    @PrimaryKey (autoGenerate = true)
    val id : Long = 0,
    val name : String?,
    val description : String?,
    val picture: String?
)
