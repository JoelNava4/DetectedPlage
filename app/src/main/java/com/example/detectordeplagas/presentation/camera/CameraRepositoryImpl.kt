package com.example.detectordeplagas.presentation.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.domain.camera.model.Foto
import com.example.domain.camera.repository.CameraRepository
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(
    private val cameraXManager: CameraXManager
) : CameraRepository {

    override suspend fun iniciarPreview() {
        // La vista y el lifecycle se los pasaremos desde la UI
        // Aquí no hacemos nada directo, la UI llamará a cameraXManager
    }

    override suspend fun detenerPreview() {
        cameraXManager.detenerPreview()
    }

    override suspend fun capturarFoto(): Foto {
        val bytes = cameraXManager.capturarFoto()
        return Foto(
            bytes = bytes,
            timestamp = System.currentTimeMillis()
        )
    }

    // Helper para que la UI pueda iniciar el preview
    suspend fun iniciarPreviewEnVista(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        cameraXManager.iniciarPreview(previewView, lifecycleOwner)
    }
}