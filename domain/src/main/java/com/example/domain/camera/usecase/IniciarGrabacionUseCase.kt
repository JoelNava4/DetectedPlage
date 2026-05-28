package com.example.domain.camera.usecase

import com.example.domain.camera.repository.VideoRepository

class IniciarGrabacionUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke() {
        videoRepository.iniciarGrabacion()
    }
}