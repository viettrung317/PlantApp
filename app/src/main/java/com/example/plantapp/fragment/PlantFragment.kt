package com.example.plantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.StarAdapter
import com.example.plantapp.Adapter.TypeAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentPlantBinding
import com.example.plantapp.model.Plant
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PlantFragment : Fragment() {
    private lateinit var binding:FragmentPlantBinding
    private val mAuth: FirebaseAuth = Firebase.auth
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference()
    private val userfb: FirebaseUser =mAuth.currentUser!!
    private val uid:String=userfb.uid
    private lateinit var user: User;
    private var plant:Plant?=Plant()
    private var listType= mutableListOf<String>()
    private var listStar= mutableListOf<Int>()
    private var listLike= mutableListOf<String>()
    private var listPlant= mutableListOf<Plant>()
    private var like:Boolean=false
    private var positionSpecies:Int = 0
    private var positionPlant:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPlantBinding.inflate(layoutInflater,container,false)
        getUser()
        return binding.root
    }

    private fun getUser() {
        ref.child("User").child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user= snapshot.getValue(User::class.java)!!
                getData(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setEvents(
        user: User,
        plant: Plant,
        like: Boolean,
        positionSpecies: Int,
        positionPlant: Int
    ) {
        binding.imagebackPlant.setOnClickListener{
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                // Thực hiện hành động khác nếu cần thiết.
            }
        }
        binding.imageLikePlant.setOnClickListener{
            if (like) {
                deletelike(user,plant,positionSpecies,positionPlant)
            } else {
                addLike(user,plant,positionSpecies,positionPlant)
            }
        }
    }

    private fun addLike(user: User, plant: Plant, positionSpecies: Int, positionPlant: Int) {
        if(user.listPlant!=null) {
            listPlant = user.listPlant!!
        }else{
            listPlant= mutableListOf<Plant>()
        }
        listPlant.add(plant)
        user.listPlant=listPlant
        if(plant.listLike!=null){
            listLike=plant.listLike!!
        }else{
            listLike= mutableListOf<String>()
        }
        user.email?.let { listLike.add(it) }
        plant.listLike=listLike
        ref.child("Data").child("Species").child(positionSpecies.toString()).child("listPlant").child(positionPlant.toString()).updateChildren(plant.toMap()).addOnCompleteListener{
            if(it.isSuccessful){
                ref.child("User").child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                    if(it.isSuccessful){
                        binding.imageLikePlant.setImageResource(R.drawable.ellipse3)
                        like=true
                        setEvents(user,plant!!,like,positionSpecies,positionPlant)
                    }
                }
            }
        }


    }

    private fun deletelike(user: User, plant: Plant, positionSpecies: Int, positionPlant: Int) {
        if(user.listPlant!=null) {
            listPlant = user.listPlant!!
        }else{
            listPlant= mutableListOf<Plant>()
        }
        listPlant.remove(plant)
        user.listPlant=listPlant
        if(plant.listLike!=null){
            listLike=plant.listLike!!
        }else{
            listLike= mutableListOf<String>()
        }
        user.email?.let { listLike.remove(it) }
        plant.listLike=listLike
        ref.child("Data").child("Species").child(positionSpecies.toString()).child("listPlant").child(positionPlant.toString()).updateChildren(plant.toMap()).addOnCompleteListener{
            if(it.isSuccessful){
                ref.child("User").child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                    if(it.isSuccessful){
                        binding.imageLikePlant.setImageResource(R.drawable.ellipse4)
                        like=false
                        setEvents(user,plant!!,like,positionSpecies,positionPlant)
                    }
                }
            }
        }
    }

    private fun getData(user: User) {
        val bundle=arguments
        plant=bundle?.getSerializable("plant") as Plant?
        positionSpecies= bundle?.getInt("positionSpecies")!!
        positionPlant= bundle.getInt("positionPlant")!!
        if(plant?.type!=null) {
            listType = plant?.type!!
        }else{
            listType= mutableListOf()
        }
        val layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.rcvTypePlant.layoutManager=layoutManager
        val adapter=TypeAdapter(listType)
        binding.rcvTypePlant.adapter=adapter
        Picasso.get().load(plant?.imagePlant).into(binding.imagePlant)
        val Star:Int=plant?.star!!.toInt()
        val noStar:Int=5-Star
        for (i in 0 until Star){
            listStar.add(R.drawable.group5)
        }
        for(i in 0 until noStar){
            listStar.add(R.drawable.group3_4)
        }
        val layoutManager1= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.rcvStar.layoutManager=layoutManager1
        val adapter1= StarAdapter(listStar)
        binding.rcvStar.adapter=adapter1
        binding.txtStar.text=plant?.star.toString()
        binding.txtKingDomPlant.text=plant?.kingDom
        binding.txtFamilyPlant.text=plant?.family
        binding.txtDescriptionPlant.text=plant?.description
        if(plant?.listLike!=null){
            for (userLike:String in plant?.listLike!!){
                if (userLike.equals(user.email)){
                    like=true
                }
            }
        }
        else{
            like=false
        }
        if(like){
            binding.imageLikePlant.setImageResource(R.drawable.ellipse3)
        }
        else{
            binding.imageLikePlant.setImageResource(R.drawable.ellipse4)
        }
        setEvents(user,plant!!,like,positionSpecies,positionPlant)
    }
}