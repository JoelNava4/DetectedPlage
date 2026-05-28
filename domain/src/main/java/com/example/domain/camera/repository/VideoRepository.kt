package com.example.domain.camera.repository

import com.example.domain.camera.model.Video


interface VideoRepository {
    suspend fun iniciarGrabacion()

    suspend fun detenerGrabacion(): Video
}