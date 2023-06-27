package com.example.plantapp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.ListTypeAdapter
import com.example.plantapp.Adapter.PhotographyAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentHomePageBinding
import com.example.plantapp.model.Plant
import com.example.plantapp.model.Type
import com.example.plantapp.model.User
import com.example.plantapp.viewModel.ViewModel
import com.squareup.picasso.Picasso

class HomePageFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentHomePageBinding

    private lateinit var user: User;
    private var listPlant= mutableListOf<Plant>()
    private var listTypePlant= mutableListOf<Type>()
    private val REQUESTCODE_CAMERA = 777


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomePageBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData();
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap = (data!!.extras!!["data"] as Bitmap?)!!
                showAddPlantFragment(user,bitmap)
            } catch (e: Exception) {
                Log.e("erro", "" + e)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun getData(){
        viewModel.getUser().observe(viewLifecycleOwner) { listUser ->
            user=listUser.get(0)
            viewModel.getPlant().observe(viewLifecycleOwner){listpl->
                listPlant=listpl as MutableList<Plant>
                setData(user,listPlant)
            }
            viewModel.loadSpecies()
        }
        viewModel.loadUser()
    }

    private fun setData(user: User, listPlant: MutableList<Plant>) {
        binding.helloUsername.setText("Hello "+user.userName)
        Picasso.get().load(user.imagesource).into(binding.imgAvatarUser);
        getListType(listPlant)
        getListPhoto(user.listPhotoGraphy);
        binding.btnAdd.setOnClickListener{
            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(
                camera,
                REQUESTCODE_CAMERA
            )
        }
        binding.btnSpecies.setOnClickListener{
            val speciesFragment = SpeciesFragment()
            val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayout)
            fragmentLayout?.let {
                val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
                fragmentManager.replace(it.id, speciesFragment)
                fragmentManager.addToBackStack(null)
                fragmentManager.commit()
            }
         }
        binding.btnArticles.setOnClickListener{
            val bundle=Bundle()
            bundle.putSerializable("user",user)
            val listArticlesFragment = ListArticlesFragment()
            listArticlesFragment.arguments=bundle
            val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayout)
            fragmentLayout?.let {
                val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
                fragmentManager.replace(it.id, listArticlesFragment)
                fragmentManager.addToBackStack(null)
                fragmentManager.commit()
            }
        }

    }

    private fun getListType(listPlant: MutableList<Plant>) {
        val miniPlants = mutableListOf<Plant>()
        val cutePlants = mutableListOf<Plant>()
        val dangerPlants = mutableListOf<Plant>()
        val decorationPlants = mutableListOf<Plant>()
        val vegetablePlants = mutableListOf<Plant>()
        for (plant in listPlant) {
            if ("mini" in plant.type.orEmpty()) {
                miniPlants.add(plant)
            }
            if ("cute" in plant.type.orEmpty()) {
                cutePlants.add(plant)
            }
            if ("danger" in plant.type.orEmpty()) {
                dangerPlants.add(plant)
            }
            if ("decoration" in plant.type.orEmpty()) {
                decorationPlants.add(plant)
            }
            if ("vegetable" in plant.type.orEmpty()) {
                vegetablePlants.add(plant)
            }
        }

        listTypePlant.add(Type("Mini", R.drawable.mini,miniPlants.size))
        listTypePlant.add(Type("Cute", R.drawable.cute,cutePlants.size))
        listTypePlant.add(Type("Danger", R.drawable.danger,dangerPlants.size))
        listTypePlant.add(Type("Decoration", R.drawable.deco,decorationPlants.size))
        listTypePlant.add(Type("Vegetable", R.drawable.vegetable,vegetablePlants.size))
        val linearLayoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rcvPlantTypes.layoutManager=linearLayoutManager
        binding.rcvPlantTypes.adapter=listTypePlant?.let { ListTypeAdapter(it) }

    }

    private fun getListPhoto(listPhotoGraphy: MutableList<Plant>?) {
        val layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.rcvPhoto.layoutManager = layoutManager
        binding.rcvPhoto.adapter= listPhotoGraphy?.let { PhotographyAdapter(it) }
    }

    fun showAddPlantFragment(user :User,bitmap: Bitmap) {
        val bundle = Bundle()
        bundle.putSerializable("user",user)
        bundle.putParcelable("image", bitmap)
        val addPlantFragment = AddPlantFragment()
        addPlantFragment.arguments = bundle
        val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayout)
        fragmentLayout?.let {
            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            fragmentManager.replace(it.id, addPlantFragment)
            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        }
    }





}