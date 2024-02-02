package com.example.sprint8.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sprint8.data.db.dao.NewPlaylistDao
import com.example.sprint8.data.db.dao.TrackDao
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, NewPlaylistEntity::class ])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun newPlayListDao(): NewPlaylistDao
}