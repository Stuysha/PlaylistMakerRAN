package com.example.sprint8.domain.player

import com.example.sprint8.data.player.PlayerRepositoryInterface

class PlayerInteractor(private val playerRepository: PlayerRepositoryInterface) :
    PlayerInteractorInterface {

    override fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    ) = playerRepository.preparePlayer(
        url, setOnPreparedListener, setOnCompletionListener
    )

    override fun startPlayer() = playerRepository.startPlayer()
    override fun pausePlayer() = playerRepository.pausePlayer()
    override fun mediaPlayerRelease() = playerRepository.mediaPlayerRelease()
}

interface PlayerInteractorInterface {
    fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    )

    fun startPlayer()
    fun pausePlayer()
    fun mediaPlayerRelease()
}