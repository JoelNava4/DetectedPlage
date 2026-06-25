package com.example.detectordeplagas

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.detectordeplagas.Porcesamiento.ProcessingFragment
import com.example.detectordeplagas.galeria.GallerySelectorFragment
import com.example.detectordeplagas.presentation.camera.view.CameraFragment
import com.example.domain.Galeria.MediaFile
import com.example.domain.Galeria.MediaType
import java.io.File

class MainActivity : AppCompatActivity() {

    private var mediaSeleccionada: MediaFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamara = findViewById<Button>(R.id.btnCamara)
        val btnGaleria = findViewById<Button>(R.id.btnGaleria)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)

        // 📸 Abrir cámara
        btnCamara.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, CameraFragment())
                .addToBackStack(null)
                .commit()
        }

        // 🖼️ Abrir nuestro selector personalizado
        btnGaleria.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, GallerySelectorFragment())
                .addToBackStack(null)
                .commit()
        }

        // 🤖 Procesar imagen o video seleccionado
        btnProcesar.setOnClickListener {
            mediaSeleccionada?.let { media ->
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.contenedor,
                        ProcessingFragment(media.bytes, media.type == MediaType.VIDEO)
                    )
                    .addToBackStack(null)
                    .commit()
            } ?: Toast.makeText(
                this,
                "Primero selecciona una imagen o video",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 🔥 Esta función la llama el selector cuando el usuario toca una foto
    fun setMediaSeleccionadaDesdePath(path: String) {
        val bytes = File(path).readBytes()
        val media = MediaFile(bytes, MediaType.IMAGE)
        mediaSeleccionada = media
        mostrarEnPantalla(media)
    }

    // 👀 Mostrar preview en pantalla
    private fun mostrarEnPantalla(media: MediaFile?) {
        if (media == null) {
            Toast.makeText(this, "Error al cargar archivo", Toast.LENGTH_SHORT).show()
            return
        }

        val imageView = findViewById<ImageView>(R.id.previewImage)
        val videoView = findViewById<VideoView>(R.id.previewVideo)

        if (media.type == MediaType.IMAGE) {
            videoView.visibility = VideoView.GONE
            imageView.visibility = ImageView.VISIBLE

            val bitmap = BitmapFactory.decodeByteArray(media.bytes, 0, media.bytes.size)
            imageView.setImageBitmap(bitmap)

        } else {
            imageView.visibility = ImageView.GONE
            videoView.visibility = VideoView.VISIBLE

            val tempFile = File(cacheDir, "tempVideo.mp4")
            tempFile.writeBytes(media.bytes)

            videoView.setVideoPath(tempFile.absolutePath)
            videoView.start()
        }
    }
}
