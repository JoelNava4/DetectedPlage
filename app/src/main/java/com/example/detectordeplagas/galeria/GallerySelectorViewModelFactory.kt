package com.example.detectordeplagas.galeria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GallerySelectorViewModelFactory(
    private val dataSource: GalleryLocalDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GallerySelectorViewModel(dataSource) as T
    }
}
