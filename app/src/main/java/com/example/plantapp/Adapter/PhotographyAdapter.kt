package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R
import com.example.plantapp.model.Plant
import com.squareup.picasso.Picasso

class PhotographyAdapter(private val listPhoto: MutableList<Plant>):
    RecyclerView.Adapter<PhotographyAdapter.PlantPhotoHolder>() {

    class PlantPhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img:ImageView=itemView.findViewById(R.id.imagephotoItem)
        val type: TextView = itemView.findViewById(R.id.txtTypePhotoItem)
    }
    // Override phương thức onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantPhotoHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemphotographylayout, parent, false)

        return PlantPhotoHolder(itemView)
    }

    // Override phương thức onBindViewHolder
    override fun onBindViewHolder(holder: PlantPhotoHolder, position: Int) {
        val currentItem = listPhoto[position]

        Picasso.get().load(currentItem.imagePlant).into(holder.img)
        holder.type.text = currentItem.type.toString()
    }

    // Override phương thức getItemCount
    override fun getItemCount() = listPhoto.size
}