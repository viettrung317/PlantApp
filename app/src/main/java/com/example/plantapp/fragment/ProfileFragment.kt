package com.example.plantapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.example.plantapp.R
import com.example.plantapp.account.LoginActivity
import com.example.plantapp.databinding.FragmentProfileBinding
import com.example.plantapp.model.User
import com.example.plantapp.viewModel.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private lateinit var viewModel:ViewModel
    private lateinit var binding:FragmentProfileBinding
    private val mAuth: FirebaseAuth = Firebase.auth
    private var address:String=""
    private lateinit var user: User;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(layoutInflater,container,false)
        viewModel= ViewModelProvider(this).get(ViewModel::class.java)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle=arguments
        address= bundle?.get("address") as String
        getData()
    }

    private fun getData() {
        viewModel.getUser().observe(viewLifecycleOwner) { listUser ->
            user=listUser.get(0)
            replaceFragment(ArticlesProfileFragment())
            setData(user)
        }
        viewModel.loadUser()
    }

    private fun setData(user: User) {
        binding.txtNameProFile.text=user.userName
        binding.txtLocation.text=address
        Picasso.get().load(user.imagesource).into(binding.imgAvatarUserProFile)
        binding.NavigationViewProfile.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_articles -> replaceFragment(ArticlesProfileFragment())
                R.id.menu_species -> replaceFragment(SpeciesProfileFragment())
                else ->{

                }
            }
            true
        }
        binding.btnLogout.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentLayout = requireActivity().findViewById<FrameLayout>(com.example.plantapp.R.id.fragmentlayoutProfile)
        fragmentLayout?.let {
            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            fragmentManager.replace(it.id, fragment)
            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        }
    }


}