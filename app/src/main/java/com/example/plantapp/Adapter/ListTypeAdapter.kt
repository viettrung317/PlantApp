package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R
import com.example.plantapp.model.Type

class ListTypeAdapter (private val listType: MutableList<Type>):
    RecyclerView.Adapter<ListTypeAdapter.ListTypeHolder>() {

    class ListTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.txtTypePlantRvc)
        val quantity: TextView = itemView.findViewById(R.id.txtQuantityPlant)
        val img:ImageView=itemView.findViewById(R.id.imgTypePlantRcv)
    }
    // Override phương thức onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTypeHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_type_layout, parent, false)

        return ListTypeHolder(itemView)
    }

    // Override phương thức onBindViewHolder
    override fun onBindViewHolder(holder: ListTypeHolder, position: Int) {
        val currentItem = listType[position]
        holder.type.text = currentItem.type+" Plants"
        holder.quantity.text= currentItem.size.toString()+" Types of Plants"
        holder.img.setImageResource(currentItem.img!!)
    }

    // Override phương thức getItemCount
    override fun getItemCount() = listType.size
}