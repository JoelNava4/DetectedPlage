package com.example.detectordeplagas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.detectordeplagas.presentation.camera.view.CameraFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAbrirCamara = findViewById<Button>(R.id.btnAbrirCamara)

        btnAbrirCamara.setOnClickListener {

            // Confirmación de que el botón funciona
            // (puedes borrar esto si quieres)
            // Toast.makeText(this, "CLICK DETECTADO", Toast.LENGTH_SHORT).show()

            // Abrir el fragmento de la cámara
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, CameraFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
