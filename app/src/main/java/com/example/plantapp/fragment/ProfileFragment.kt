package com.example.plantapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentProfileBinding
import com.example.plantapp.model.User
import com.example.plantapp.viewModel.ListArticlesViewModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private lateinit var viewModel:ListArticlesViewModel
    private lateinit var binding:FragmentProfileBinding
    private var address:String=""
    private lateinit var user: User;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(layoutInflater,container,false)
        viewModel= ViewModelProvider(this).get(ListArticlesViewModel::class.java)
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