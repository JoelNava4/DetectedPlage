package com.example.domain.Galeria

data class MediaFile(
    val bytes: ByteArray,
    val type: MediaType,
    val name: String? = null
)
