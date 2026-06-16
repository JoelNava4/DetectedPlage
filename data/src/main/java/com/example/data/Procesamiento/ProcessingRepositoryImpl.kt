package com.example.data.Procesamiento

import com.example.domain.Procesamiento.DetectionResult
import com.example.domain.Procesamiento.ProcessingRepository
import kotlin.random.Random

class ProcessingRepositoryImpl : ProcessingRepository {

    private val plagas = listOf(
        "Pulgón",
        "Mosca Blanca",
        "Araña Roja",
        "Trips",
        "Cochinilla"
    )

    override suspend fun procesarImagen(bytes: ByteArray): DetectionResult {
        return generarResultado("imagen")
    }

    override suspend fun procesarVideo(bytes: ByteArray): DetectionResult {
        return generarResultado("video")
    }

    private fun generarResultado(tipo: String): DetectionResult {
        val plaga = plagas.random()
        val confianza = Random.nextFloat() * (0.9f - 0.6f) + 0.6f

        val recomendacion = when (plaga) {
            "Pulgón" -> "Aplicar jabón potásico cada 3 días."
            "Mosca Blanca" -> "Usar trampas cromáticas amarillas."
            "Araña Roja" -> "Aumentar humedad y aplicar acaricida."
            "Trips" -> "Aplicar extracto de neem."
            "Cochinilla" -> "Limpiar con algodón y alcohol."
            else -> "Monitorear la planta."
        }

        return DetectionResult(plaga, confianza, recomendacion)
    }
}