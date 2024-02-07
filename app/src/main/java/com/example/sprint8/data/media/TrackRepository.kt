package com.example.sprint8.data.media

import com.example.sprint8.data.converters.TrackConverter
import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.domain.interfaces.TrackRepositoryInterface
import com.example.sprint8.domain.models.Track

class TrackRepository(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TrackConverter,
) : TrackRepositoryInterface {
    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(movieDbConvertor.map(track, null))
    }

}