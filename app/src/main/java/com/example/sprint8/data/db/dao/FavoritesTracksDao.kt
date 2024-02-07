package com.example.sprint8.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sprint8.data.db.entity.FavoritesTracksEntity

@Dao
interface FavoritesTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesTracks(newPlaylist: FavoritesTracksEntity)

    @Delete
    suspend fun deleteFavoritesTracks(newPlaylist: FavoritesTracksEntity)

    @Query("SELECT * FROM FavoritesTracksEntity")
    suspend fun getFavoritesTracks(): List<FavoritesTracksEntity>

    @Query("SELECT * FROM FavoritesTracksEntity WHERE id == :id")
    suspend fun getFavoritesTracks(id: Long): List<FavoritesTracksEntity>
}