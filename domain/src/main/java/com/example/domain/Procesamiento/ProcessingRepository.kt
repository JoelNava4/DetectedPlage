package com.example.domain.Procesamiento

interface ProcessingRepository {
    suspend fun procesarImagen(bytes: ByteArray): DetectionResult
    suspend fun procesarVideo(bytes: ByteArray): DetectionResult
}