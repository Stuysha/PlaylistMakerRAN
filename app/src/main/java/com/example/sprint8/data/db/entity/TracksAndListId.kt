package com.example.sprint8.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public class TracksAndListId (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val idPlayList : Long,
    val idTrack : Long
)
