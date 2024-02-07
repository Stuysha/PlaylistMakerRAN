package com.example.sprint8.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sprint8.data.db.entity.NewPlaylistEntity
import com.example.sprint8.data.db.entity.TracksAndListIdEntity

@Dao
interface NewPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

    @Delete
    suspend fun deleteNewPlaylist(newPlaylist: List<NewPlaylistEntity>)

    @Query("SELECT * FROM NewPlaylistEntity")
    suspend fun getNewPlaylist(): List<NewPlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracksAndListId(newPlaylist: TracksAndListIdEntity)

    @Delete
    suspend fun deleteTracksAndListId(newPlaylist: List<TracksAndListIdEntity>)

    @Query("DELETE FROM TracksAndListIdEntity WHERE idPlayList == :idPlaylist AND idTrack == :idTrack")
    suspend fun deleteTracksAndListId(idPlaylist: Long, idTrack: Long)

    @Query("SELECT * FROM TracksAndListIdEntity")
    suspend fun getTracksAndListId(): List<TracksAndListIdEntity>


    @Query("SELECT * FROM TracksAndListIdEntity WHERE idPlayList == :idPlaylist AND idTrack == :idTrack")
    suspend fun getTracksAndListId(idPlaylist: Long, idTrack: Long): List<TracksAndListIdEntity>

    @Query("SELECT * FROM NewPlaylistEntity WHERE id == :id")
    suspend fun getPlayList(id: Long): NewPlaylistEntity

    @Query("SELECT * FROM TracksAndListIdEntity WHERE idPlayList == :idPlaylist")
    suspend fun getTracksAndListId(idPlaylist: Long): List<TracksAndListIdEntity>

    @Query("SELECT * FROM TracksAndListIdEntity WHERE idTrack == :idTrack")
    suspend fun getTracksAndListIdByTrack(idTrack: Long): List<TracksAndListIdEntity>
}