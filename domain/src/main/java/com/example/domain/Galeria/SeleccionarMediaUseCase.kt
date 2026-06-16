package com.example.domain.Galeria

import com.example.domain.Galeria.GalleryRepository
import com.example.domain.Galeria.MediaFile

class SeleccionarMediaUseCase(
    private val repository: GalleryRepository
) {
    suspend operator fun invoke(bytes: ByteArray, nombre: String?): MediaFile? {
        return repository.obtenerMedia(bytes, nombre)
    }
}
