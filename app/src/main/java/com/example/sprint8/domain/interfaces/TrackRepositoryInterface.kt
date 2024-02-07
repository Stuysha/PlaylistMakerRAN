package com.example.sprint8.domain.interfaces

import com.example.sprint8.domain.models.Track

interface TrackRepositoryInterface {
    suspend fun insertTrack(track: Track)
}