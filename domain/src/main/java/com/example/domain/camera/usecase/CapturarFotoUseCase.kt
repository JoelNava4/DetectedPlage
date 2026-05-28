package com.example.domain.camera.usecase

import com.example.domain.camera.model.Foto
import com.example.domain.camera.repository.CameraRepository

class CapturarFotoUseCase(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(): Foto {
        return cameraRepository.capturarFoto()
    }
}