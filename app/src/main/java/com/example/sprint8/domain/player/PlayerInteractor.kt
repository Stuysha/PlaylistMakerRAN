package com.example.sprint8.domain.player

import com.example.sprint8.data.player.PlayerRepository

class PlayerInteractor(private val playerRepository: PlayerRepository) {

    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    ) = playerRepository.preparePlayer(
        url, setOnPreparedListener, setOnCompletionListener
    )

    fun startPlayer() = playerRepository.startPlayer()
    fun pausePlayer() = playerRepository.pausePlayer()
    fun mediaPlayerRelease() = playerRepository.mediaPlayerRelease()
}