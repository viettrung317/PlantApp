package com.example.plantapp.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R
import com.example.plantapp.fragment.SpeciesFragment
import com.example.plantapp.model.Species
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

class SpeciesAdapter(private val listSpecies: List<Species>, private val listener: SpeciesFragment) : RecyclerView.Adapter<SpeciesAdapter.SpeciesViewHolder>(), SectionIndexer,
    FastScrollRecyclerView.SectionedAdapter {
    inner class SpeciesViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView) {
        val tv_alphabet=itemView.findViewById<TextView>(R.id.tv_alphabet)
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

    private var mSectionPositions: ArrayList<Int> = ArrayList()

    override fun getSections(): Array<Any> {
        val sections: MutableList<String> = ArrayList(26)
        mSectionPositions = ArrayList(26)
        for ((index,species:Species) in listSpecies.withIndex()) {
            val section = species.nameSpecies.toString()[0].toUpperCase().toString()
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions.add(index)
            }
        }
        return sections.toTypedArray()
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions[sectionIndex]
    }
    override fun getSectionForPosition(position: Int): Int {
        var sectionIndex = 0
        for (i in mSectionPositions.indices) {
            if (position >= mSectionPositions[i]) {
                sectionIndex = i
            } else {
                break
            }
        }
        return sectionIndex
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_species_layout, parent, false)

        return SpeciesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpeciesViewHolder, position: Int) {
        val currentItem = listSpecies[position]
        holder.tv_alphabet.text = currentItem.nameSpecies
    }

    override fun getItemCount(): Int {
        return listSpecies.size
    }

    override fun getSectionName(position: Int): String {
        return listSpecies[position].nameSpecies.toString()[0].toUpperCase().toString()
    }
}


