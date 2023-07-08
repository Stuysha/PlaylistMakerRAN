package com.example.sprint8.creator

import com.example.sprint8.data.player.PlayerRepository
import com.example.sprint8.domain.player.PlayerInteractor

object CreatorMediaObject {

    fun createPlayerRepository(): PlayerRepository {
        return PlayerRepository(android.media.MediaPlayer())
    }


    fun createPlayerInteractor(): PlayerInteractor {
        return PlayerInteractor(
            createPlayerRepository()
        )
    }
}