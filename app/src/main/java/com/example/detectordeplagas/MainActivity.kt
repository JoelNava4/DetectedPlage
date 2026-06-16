package com.example.detectordeplagas

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import android.graphics.BitmapFactory
import android.widget.VideoView
import com.example.detectordeplagas.Porcesamiento.ProcessingFragment
import com.example.detectordeplagas.galeria.GalleryRepositoryImpl
import com.example.detectordeplagas.presentation.camera.view.CameraFragment
import com.example.domain.Galeria.MediaFile
import com.example.domain.Galeria.MediaType
import java.io.File
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repo: GalleryRepositoryImpl

    private val seleccionarMedia =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { mostrarPreview(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repo = GalleryRepositoryImpl(contentResolver)

        val btnCamara = findViewById<Button>(R.id.btnCamara)
        val btnGaleria = findViewById<Button>(R.id.btnGaleria)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)

        btnCamara.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, CameraFragment())
                .addToBackStack(null)
                .commit()
        }

        btnGaleria.setOnClickListener {
            seleccionarMedia.launch("image/* video/*")
        }

        btnProcesar.setOnClickListener {
            mediaSeleccionada?.let { media ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contenedor, ProcessingFragment(media.bytes, media.type == MediaType.VIDEO))
                    .addToBackStack(null)
                    .commit()
            } ?: Toast.makeText(this, "Primero selecciona una imagen o video", Toast.LENGTH_SHORT).show()
        }
    }

    private var mediaSeleccionada: MediaFile? = null

    private fun mostrarPreview(uri: Uri) {
        lifecycleScope.launch {
            val media = repo.obtenerMediaDesdeUri(uri)
            mediaSeleccionada = media
            mostrarEnPantalla(media)
        }
    }

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

