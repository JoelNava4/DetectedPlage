package com.example.detectordeplagas.Porcesamiento

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.data.Procesamiento.ProcessingRepositoryImpl
import com.example.detectordeplagas.R
import com.example.domain.Procesamiento.ProcesarMediaUseCase
import kotlinx.coroutines.flow.collectLatest

class ProcessingFragment(
    private val bytes: ByteArray,
    private val esVideo: Boolean
) : Fragment(R.layout.fragment_processing) {

    private val viewModel: ProcessingViewModel by viewModels {
        val repo = ProcessingRepositoryImpl()
        val useCase = ProcesarMediaUseCase(repo)
        ProcessingViewModelFactory(useCase)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val txtPlaga = view.findViewById<TextView>(R.id.txtPlaga)
        val txtConfianza = view.findViewById<TextView>(R.id.txtConfianza)
        val txtRecomendacion = view.findViewById<TextView>(R.id.txtRecomendacion)

        lifecycleScope.launchWhenStarted {
            viewModel.resultado.collectLatest { res ->
                if (res != null) {
                    txtPlaga.text = "Plaga detectada: ${res.plaga}"
                    txtConfianza.text = "Confianza: ${(res.confianza * 100).toInt()}%"
                    txtRecomendacion.text = "Recomendación: ${res.recomendacion}"
                }
            }
        }

        viewModel.procesar(bytes, esVideo)
    }
}