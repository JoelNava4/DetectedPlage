package com.example.detectordeplagas.galeria

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.detectordeplagas.R
import com.example.domain.Galeria.MediaType
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel: GalleryViewModel by viewModels {
        val repo = GalleryRepositoryImpl(requireContext().contentResolver)
        GalleryViewModelFactory(repo)
    }

    // 📸 Photo Picker oficial (Android 13+)
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                viewModel.cargarDesdeUri(uri)
            } else {
                Toast.makeText(requireContext(), "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                viewModel.cargarDesdeUri(uri)
            } else {
                Toast.makeText(requireContext(), "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pedirPermisos()

        val previewImage = view.findViewById<ImageView>(R.id.previewImage)
        val previewVideo = view.findViewById<VideoView>(R.id.previewVideo)

        // 📸 Seleccionar imagen con Photo Picker oficial
        view.findViewById<Button>(R.id.btnFoto).setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // 🎥 Seleccionar video con Photo Picker oficial
        view.findViewById<Button>(R.id.btnVideo).setOnClickListener {
            pickVideoLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.media.collectLatest { media ->
                if (media != null) {

                    if (media.type == MediaType.IMAGE) {
                        previewVideo.visibility = View.GONE
                        previewImage.visibility = View.VISIBLE

                        previewImage.setImageBitmap(
                            BitmapFactory.decodeByteArray(media.bytes, 0, media.bytes.size)
                        )

                    } else {
                        previewImage.visibility = View.GONE
                        previewVideo.visibility = View.VISIBLE

                        val tempFile = File(requireContext().cacheDir, "tempVideo.mp4")
                        tempFile.writeBytes(media.bytes)

                        previewVideo.setVideoPath(tempFile.absolutePath)
                        previewVideo.start()
                    }
                }
            }
        }
    }

    // 🔥 Permisos Android 13+
    private fun pedirPermisos() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            ),
            200
        )
    }
}
