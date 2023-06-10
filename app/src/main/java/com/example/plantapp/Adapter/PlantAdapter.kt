package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R
import com.example.plantapp.fragment.ListPlantFragment
import com.example.plantapp.fragment.SpeciesFragment
import com.example.plantapp.model.Plant
import com.squareup.picasso.Picasso

class PlantAdapter(private val listPlant: MutableList<Plant>, private val listener: ListPlantFragment): RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamePlantItem = itemView.findViewById<TextView>(R.id.txtNamePlantItem)
        val txtKingDomItem = itemView.findViewById<TextView>(R.id.txtKingDomItem)
        val txtFamilyItem = itemView.findViewById<TextView>(R.id.txtFamilyItem)
        val txtDescriptionItem = itemView.findViewById<TextView>(R.id.txtDescriptionItem)
        val imageplantItem = itemView.findViewById<ImageView>(R.id.imageplantItem)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemplantlayout, parent, false)
        return PlantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val item=listPlant[position]
        Picasso.get().load(item.imagePlant).into(holder.imageplantItem)
        holder.txtNamePlantItem.text = item.plantName
        holder.txtKingDomItem.text=item.kingDom
        holder.txtFamilyItem.text=item.family
        if(item.description?.length!! >54){
            val text=item.description!!.substring(0,55)+"..."
            holder.txtDescriptionItem.text=text
        }else{
            holder.txtDescriptionItem.text=item.description
        }
    }

    override fun getItemCount(): Int {
        return listPlant.size
    }
}