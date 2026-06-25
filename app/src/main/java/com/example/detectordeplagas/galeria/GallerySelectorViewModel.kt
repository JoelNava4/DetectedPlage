package com.example.detectordeplagas.galeria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GallerySelectorViewModel(
    private val dataSource: GalleryLocalDataSource
) : ViewModel() {

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images

    fun loadImages() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                dataSource.getAllImages()
            }
            _images.value = lista
        }
    }
}
