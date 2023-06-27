package com.example.plantapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.PlantAdapter
import com.example.plantapp.Adapter.SelecterAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentSpeciesProfileBinding
import com.example.plantapp.model.*
import com.example.plantapp.viewModel.ViewModel

class SpeciesProfileFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentSpeciesProfileBinding
    private var listPlant= mutableListOf<Plant>()
    private lateinit var user: User;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSpeciesProfileBinding.inflate(layoutInflater,container,false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        viewModel.getUser().observe(viewLifecycleOwner) { listUser ->
            user=listUser.get(0)
            setData(user)
        }
        viewModel.loadUser()
    }

    private fun setData(user: User) {
        getList(user)
    }

    private fun getList(user: User) {
        var selecterAdapter = SelecterAdapter(requireContext(), R.layout.item_selecterlayout, getlist())
        binding.spinner1.setAdapter(selecterAdapter)
        binding.spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (selecterAdapter.getItem(i)?.item.equals("Your plant")) {
                    if(user.listPhotoGraphy!=null){
                        listPlant=user.listPhotoGraphy!!
                        getlistPlant(listPlant)
                    }
                } else if (selecterAdapter.getItem(i)?.item
                        .equals("Plant liked")
                ) {
                    if(user.listPlant!=null){
                        listPlant=user.listPlant!!
                        getlistPlant(listPlant)
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
    }

    private fun getlistPlant(listPlant: MutableList<Plant>) {
        val linearLayoutManager= LinearLayoutManager(requireContext())
        binding.rcvSpeciesProfile.layoutManager=linearLayoutManager
        val adapter= PlantAdapter(listPlant, ListPlantFragment())
        binding.rcvSpeciesProfile.adapter=adapter
    }

    private fun getlist(): List<Selecter?> {
        var list= mutableListOf<Selecter>()
        list.add(Selecter("Your plant"))
        list.add(Selecter("Plant liked"))
        return list
    }

}