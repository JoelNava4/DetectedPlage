package com.example.detectordeplagas.galeria

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.detectordeplagas.R

class GalleryAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.Holder>() {

    private val items = mutableListOf<String>()

    fun submitList(list: List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val path = items[position]

        val bmp = BitmapFactory.decodeFile(path)
        holder.img.setImageBitmap(bmp)

        holder.itemView.setOnClickListener {
            onClick(path)
        }
    }

    override fun getItemCount() = items.size
}
