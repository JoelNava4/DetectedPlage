package com.example.detectordeplagas.galeria

import android.content.ContentResolver
import android.provider.MediaStore

class GalleryLocalDataSource(private val resolver: ContentResolver) {

    fun getAllImages(): List<String> {
        val images = mutableListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
        )

        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            var count = 0
            while (it.moveToNext() && count < 50) {  // 🔥 SOLO 50 IMÁGENES
                val path = it.getString(dataIndex)
                images.add(path)
                count++
            }
        }

        return images
    }
}
