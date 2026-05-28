package com.example.domain.camera.usecase

import com.example.domain.camera.repository.CameraRepository

class IniciarPreviewUseCase(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke() {
        cameraRepository.iniciarPreview()
    }
}