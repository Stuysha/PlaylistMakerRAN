package com.example.sprint8.domain.media

import com.example.sprint8.data.converters.TrackConverter
import com.example.sprint8.domain.interfaces.FavoriteTracksRepositoryRepositoryInterface
import com.example.sprint8.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksInteractor(
    private val repository: FavoriteTracksRepositoryRepositoryInterface,
    private val movieDbConvertor: TrackConverter,
) : FavoriteTracksInteractorInterface {
    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
            .map {
                it.map { movieDbConvertor.map(it) }
            }
    }


}

interface FavoriteTracksInteractorInterface {
    suspend fun getFavoriteTracks(): Flow<List<Track>>
}