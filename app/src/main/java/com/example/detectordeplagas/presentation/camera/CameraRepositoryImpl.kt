package com.example.detectordeplagas.presentation.camera

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.domain.camera.model.Foto
import com.example.domain.camera.repository.CameraRepository
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(
    private val cameraXManager: CameraXManager
) : CameraRepository {

    override suspend fun iniciarPreview() {
        // La UI controla el preview, aquí no hacemos nada
    }

    override suspend fun detenerPreview() {
        cameraXManager.detenerPreview()
    }

    override suspend fun capturarFoto(onResultado: (Foto?) -> Unit) {
        cameraXManager.capturarFoto { uri: Uri? ->
            if (uri != null) {
                onResultado(
                    Foto(
                        uri = uri.toString(),
                        timestamp = System.currentTimeMillis()
                    )
                )
            } else {
                onResultado(null)
            }
        }
    }

    // Helper para iniciar preview desde la UI
    suspend fun iniciarPreviewEnVista(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        cameraXManager.iniciarPreview(previewView, lifecycleOwner)
    }
}
