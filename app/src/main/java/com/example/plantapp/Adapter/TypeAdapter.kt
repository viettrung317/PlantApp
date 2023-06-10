package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R

class TypeAdapter(private val listType: MutableList<String>):
    RecyclerView.Adapter<TypeAdapter.TypeHolder>() {

    class TypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.txtTypePlant)
    }
    // Override phương thức onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_type_layout, parent, false)

        return TypeHolder(itemView)
    }

    // Override phương thức onBindViewHolder
    override fun onBindViewHolder(holder: TypeHolder, position: Int) {
        val currentItem = listType[position]
        holder.type.text = currentItem
    }

    // Override phương thức getItemCount
    override fun getItemCount() = listType.size
}