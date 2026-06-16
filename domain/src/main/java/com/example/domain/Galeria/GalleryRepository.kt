package com.example.domain.Galeria

import com.example.domain.Galeria.MediaFile

interface GalleryRepository {
    suspend fun obtenerMedia(bytes: ByteArray, nombre: String?): MediaFile?
}
