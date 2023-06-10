package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R

class StarAdapter(private val listStar: MutableList<Int>):
    RecyclerView.Adapter<StarAdapter.StarHolder>() {

    class StarHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView =itemView.findViewById(R.id.imgStar)
    }
    // Override phương thức onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_star_layout, parent, false)

        return StarHolder(itemView)
    }

    // Override phương thức onBindViewHolder
    override fun onBindViewHolder(holder: StarHolder, position: Int) {
        val currentItem = listStar[position]
        holder.img.setImageResource(currentItem)
    }

    // Override phương thức getItemCount
    override fun getItemCount() = listStar.size
}