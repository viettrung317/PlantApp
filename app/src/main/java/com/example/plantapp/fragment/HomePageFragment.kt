package com.example.plantapp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.PhotographyAdapter
import com.example.plantapp.databinding.FragmentHomePageBinding
import com.example.plantapp.model.Plant
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private val mAuth:FirebaseAuth= Firebase.auth
    private val data:FirebaseDatabase=Firebase.database
    private val ref:DatabaseReference=data.getReference("User")
    private val userfb: FirebaseUser=mAuth.currentUser!!
    private val uid:String=userfb.uid
    private lateinit var user: User;
    private val REQUESTCODE_CAMERA = 777


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomePageBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.imgAvatarUser.setOnClickListener{
            mAuth.signOut()
        }
        getData();
        return binding.root
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
        ref.child(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user= snapshot.getValue(User::class.java)!!
                setData(user);
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setData(user:User) {
        binding.helloUsername.setText("Hello "+user.userName)
        Picasso.get().load(user.imagesource).into(binding.imgAvatarUser);
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