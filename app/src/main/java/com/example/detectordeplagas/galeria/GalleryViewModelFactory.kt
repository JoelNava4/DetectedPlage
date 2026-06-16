package com.example.detectordeplagas.galeria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.detectordeplagas.galeria.GalleryRepositoryImpl

class GalleryViewModelFactory(
    private val repo: GalleryRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(repo) as T
    }
}