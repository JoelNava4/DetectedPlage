package com.example.detectordeplagas.galeria

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.detectordeplagas.MainActivity
import com.example.detectordeplagas.R
import kotlinx.coroutines.flow.collectLatest

class GallerySelectorFragment : Fragment(R.layout.fragment_gallery_selector) {

    private val viewModel: GallerySelectorViewModel by viewModels {
        val ds = GalleryLocalDataSource(requireContext().contentResolver)
        GallerySelectorViewModelFactory(ds)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerGallery)

        recycler.layoutManager = GridLayoutManager(requireContext(), 3)

        val adapter = GalleryAdapter { path ->
            (requireActivity() as MainActivity).setMediaSeleccionadaDesdePath(path)
            parentFragmentManager.popBackStack()
        }

        recycler.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.images.collectLatest { lista ->
                adapter.submitList(lista)
            }
        }

        viewModel.loadImages()
    }
}
