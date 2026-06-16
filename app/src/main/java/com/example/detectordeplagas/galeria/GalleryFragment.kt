package com.example.detectordeplagas.galeria

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.detectordeplagas.R
import com.example.domain.Galeria.MediaType
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val seleccionarMedia =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.cargarDesdeUri(it) }
        }

    private val viewModel: GalleryViewModel by viewModels {
        val repo = GalleryRepositoryImpl(requireContext().contentResolver)
        GalleryViewModelFactory(repo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val previewImage = view.findViewById<ImageView>(R.id.previewImage)
        val previewVideo = view.findViewById<VideoView>(R.id.previewVideo)

        view.findViewById<Button>(R.id.btnFoto).setOnClickListener {
            seleccionarMedia.launch("image/*")
        }

        view.findViewById<Button>(R.id.btnVideo).setOnClickListener {
            seleccionarMedia.launch("video/*")
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
}

