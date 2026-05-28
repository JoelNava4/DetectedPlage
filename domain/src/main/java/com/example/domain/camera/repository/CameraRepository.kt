package com.example.domain.camera.repository
import com.example.domain.camera.model.Foto

interface CameraRepository {
    suspend fun iniciarPreview()

    suspend fun detenerPreview()

    suspend fun capturarFoto(): Foto
}