package com.example.detectordeplagas.Porcesamiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.Procesamiento.ProcesarMediaUseCase

class ProcessingViewModelFactory(
    private val useCase: ProcesarMediaUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProcessingViewModel(useCase) as T
    }
}