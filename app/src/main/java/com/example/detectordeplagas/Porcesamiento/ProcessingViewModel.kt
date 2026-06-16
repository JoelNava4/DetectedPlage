package com.example.detectordeplagas.Porcesamiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Procesamiento.DetectionResult
import com.example.domain.Procesamiento.ProcesarMediaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProcessingViewModel(
    private val useCase: ProcesarMediaUseCase
) : ViewModel() {

    private val _resultado = MutableStateFlow<DetectionResult?>(null)
    val resultado: StateFlow<DetectionResult?> = _resultado

    fun procesar(bytes: ByteArray, esVideo: Boolean) {
        viewModelScope.launch {
            val res = useCase(bytes, esVideo)
            _resultado.value = res
        }
    }
}