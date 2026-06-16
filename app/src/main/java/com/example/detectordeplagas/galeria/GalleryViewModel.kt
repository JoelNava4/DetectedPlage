package com.example.detectordeplagas.galeria

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Galeria.MediaFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.detectordeplagas.galeria.GalleryRepositoryImpl

class GalleryViewModel(
    private val repo: GalleryRepositoryImpl
) : ViewModel() {

    private val _media = MutableStateFlow<MediaFile?>(null)
    val media: StateFlow<MediaFile?> = _media

    fun cargarDesdeUri(uri: Uri) {
        viewModelScope.launch {
            val result = repo.obtenerMediaDesdeUri(uri)
            _media.value = result
        }
    }
}