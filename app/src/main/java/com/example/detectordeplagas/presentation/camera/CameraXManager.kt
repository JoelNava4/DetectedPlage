package com.example.detectordeplagas.presentation.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXManager(
    private val context: Context
) {

    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * Inicia el preview en un PreviewView
     */
    fun iniciarPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            // 🔥 SIEMPRE EN EL HILO PRINCIPAL
            Handler(Looper.getMainLooper()).post {
                preview.setSurfaceProvider(previewView.surfaceProvider)

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Log.e("CameraXManager", "Error al iniciar preview", exc)
                }
            }

        }, ContextCompat.getMainExecutor(context)) // 🔥 EJECUTOR CORRECTO
    }

    /**
     * Detiene el preview y libera recursos
     */
    fun detenerPreview() {
        cameraExecutor.shutdown()
    }

    /**
     * Captura una foto y la guarda en la galería
     */
    fun capturarFoto(
        onFotoGuardada: (uri: Uri?) -> Unit
    ) {
        val imageCapture = imageCapture ?: run {
            Log.e("CameraX", "imageCapture es null")
            onFotoGuardada(null)
            return
        }

        val nombreArchivo = "plaga_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera") // 🔥 Xiaomi-safe
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri == null) {
            Log.e("CameraX", "resolver.insert devolvió null")
            onFotoGuardada(null)
            return
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            resolver.openOutputStream(uri)!! // 🔥 STREAM DIRECTO (MIUI-friendly)
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Error al capturar foto", exception)
                    onFotoGuardada(null)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("CameraX", "Foto guardada en: $uri")
                    onFotoGuardada(uri)
                }
            }
        )
    }

}
