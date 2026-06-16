package com.example.domain.Procesamiento

class ProcesarMediaUseCase(
    private val repo: ProcessingRepository
) {
    suspend operator fun invoke(bytes: ByteArray, esVideo: Boolean): DetectionResult {
        return if (esVideo) repo.procesarVideo(bytes)
        else repo.procesarImagen(bytes)
    }
}