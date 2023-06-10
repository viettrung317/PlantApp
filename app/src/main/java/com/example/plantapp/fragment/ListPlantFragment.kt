package com.example.plantapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.PlantAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentListPlantBinding
import com.example.plantapp.model.Plant
import com.example.plantapp.model.Species
import java.util.*


class ListPlantFragment : Fragment(),PlantAdapter.OnItemClickListener {
    private lateinit var binding:FragmentListPlantBinding
    private var species:Species?= Species()
    private var listPlant= mutableListOf<Plant>()
    private var positionSpecies:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentListPlantBinding.inflate(layoutInflater,container,false)
        getData()
        return binding.root
    }
    private fun setEvents(listPlant: MutableList<Plant>) {
        binding.imgExitPlant.setOnClickListener{
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                // Thực hiện hành động khác nếu cần thiết.
            }
        }
        binding.txtSeachPlant.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imageView112.visibility=View.GONE
                var list= mutableListOf<Plant>()
                for (plant: Plant in listPlant) {
                    if (plant.plantName?.toLowerCase(Locale.getDefault())
                            ?.contains(s.toString().toLowerCase(Locale.getDefault())) == true)

                    {
                        list.add(plant)
                    }
                }
                val adapter = PlantAdapter(list,this@ListPlantFragment)
                binding.rcvPlant.adapter = adapter

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun getData() {
        val bundle=arguments
        species=bundle?.getSerializable("species") as Species?
        positionSpecies= bundle?.getInt("positionSpecise")!!
        if(species?.listPlant!=null){
            listPlant= species?.listPlant!!
        }else{
            listPlant= mutableListOf<Plant>()
        }
        binding.txtNameSpecies.text= species!!.nameSpecies
        binding.txtName.text=species!!.nameSpecies
        binding.txtSeachPlant.hint="seach for "+species!!.nameSpecies
        binding.rcvPlant.layoutManager= LinearLayoutManager(requireContext())
        val adapter= PlantAdapter(listPlant,this@ListPlantFragment)
        binding.rcvPlant.adapter=adapter
        setEvents(listPlant)

    }

    override fun onItemClick(position: Int) {
        val plant = listPlant[position]
        val bundle = Bundle()
        bundle.putSerializable("plant",plant)
        bundle.putInt("positionPlant",position)
        bundle.putInt("positionSpecies",positionSpecies)
        val plantFragment = PlantFragment()
        plantFragment.arguments = bundle
        val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayout)
        fragmentLayout?.let {
            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            fragmentManager.replace(it.id, plantFragment)
            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        }
    }


}