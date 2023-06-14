package com.example.plantapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.plantapp.R
import com.example.plantapp.model.Selecter

class SelecterAdapter(context: Context, resource: Int, objects: List<Selecter?>) :
    ArrayAdapter<Selecter?>(context, resource, objects as List<Selecter?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_selecterlayout, parent, false)
        val txtselected =
            convertView.findViewById<View>(R.id.txtItemSelecter) as TextView
        val select: Selecter? = this.getItem(position)
        if (select != null) {
            txtselected.text = select.item
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selecterlayout, parent, false)
        val txt_select = convertView!!.findViewById<View>(R.id.txtItemSelecter) as TextView
        val select: Selecter? = this.getItem(position)
        if (select != null) {
            txt_select.text = select.item
        }
        return convertView
    }
}
