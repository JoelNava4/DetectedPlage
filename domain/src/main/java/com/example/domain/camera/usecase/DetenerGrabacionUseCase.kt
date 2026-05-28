package com.example.domain.camera.usecase

import com.example.domain.camera.model.Video
import com.example.domain.camera.repository.VideoRepository

class DetenerGrabacionUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(): Video {
        return videoRepository.detenerGrabacion()
    }
}