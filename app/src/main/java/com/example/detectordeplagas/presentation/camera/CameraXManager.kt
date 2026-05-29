package com.example.detectordeplagas.presentation.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.io.File
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

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

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

        }, cameraExecutor) // ← EJECUTOR COMPATIBLE CON API 24
    }

    /**
     * Detiene el preview y libera recursos
     */
    fun detenerPreview() {
        cameraExecutor.shutdown()
    }

    /**
     * Captura una foto y devuelve los bytes
     */
    fun capturarFoto(): ByteArray {
        val imageCapture = imageCapture ?: return ByteArray(0)

        val archivo = File.createTempFile("plaga_", ".jpg", context.cacheDir)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(archivo).build()

        var resultado: ByteArray? = null
        val lock = Object()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraXManager", "Error al capturar foto", exception)
                    synchronized(lock) {
                        resultado = null
                        lock.notify()
                    }
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bytes = archivo.readBytes()
                    synchronized(lock) {
                        resultado = bytes
                        lock.notify()
                    }
                }
            }
        )

        synchronized(lock) {
            lock.wait(5000)
        }

        return resultado ?: ByteArray(0)
    }
}
