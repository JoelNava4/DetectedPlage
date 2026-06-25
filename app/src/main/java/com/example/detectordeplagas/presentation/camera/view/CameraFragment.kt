package com.example.detectordeplagas.presentation.camera.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.detectordeplagas.R
import com.example.detectordeplagas.presentation.camera.CameraXManager

class CameraFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var cameraXManager: CameraXManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        previewView = view.findViewById(R.id.previewView)
        cameraXManager = CameraXManager(requireContext())

        view.findViewById<Button>(R.id.btnCapturar).setOnClickListener {
            cameraXManager.capturarFoto { uri ->
                if (uri != null) {
                    Toast.makeText(requireContext(), "Foto guardada en galería", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al guardar foto", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⭐ USAR EL CICLO DE VIDA CORRECTO
        cameraXManager.iniciarPreview(previewView, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraXManager.detenerPreview()
    }
}
