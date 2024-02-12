package com.example.sprint8.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sprint8.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM TrackEntity WHERE trackId == :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("SELECT * FROM TrackEntity ORDER BY addTime DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE trackId == :trackId")
    suspend fun getTrack(trackId: Long): TrackEntity?

    @Query("SELECT * FROM TrackEntity WHERE trackId IN (:trackId)")
    suspend fun getTracks(trackId: List<Long>): List<TrackEntity>
}