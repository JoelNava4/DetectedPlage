package com.example.detectordeplagas.galeria


import android.content.ContentResolver
import android.net.Uri
import com.example.domain.Galeria.GalleryRepository
import com.example.domain.Galeria.MediaFile
import com.example.domain.Galeria.MediaType

class GalleryRepositoryImpl(
    private val contentResolver: ContentResolver
) : GalleryRepository {

    // Método exclusivo de DATA (Android)
    suspend fun obtenerMediaDesdeUri(uri: Uri): MediaFile? {
        val input = contentResolver.openInputStream(uri) ?: return null
        val bytes = input.readBytes()
        val nombre = uri.lastPathSegment

        val mimeType = contentResolver.getType(uri)

        val type = when {
            mimeType?.startsWith("image") == true -> MediaType.IMAGE
            mimeType?.startsWith("video") == true -> MediaType.VIDEO
            else -> return null
        }

        return MediaFile(bytes, type, nombre)
    }

    // Implementación del dominio (sin Android)
    override suspend fun obtenerMedia(bytes: ByteArray, nombre: String?): MediaFile? {
        val type = when {
            nombre?.endsWith(".mp4") == true -> MediaType.VIDEO
            else -> MediaType.IMAGE
        }

        return MediaFile(bytes, type, nombre)
    }
}