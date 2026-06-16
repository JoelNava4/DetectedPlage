package com.example.domain.Procesamiento

data class DetectionResult(
    val plaga: String,
    val confianza: Float,
    val recomendacion: String
)
