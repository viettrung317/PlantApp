package com.example.plantapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantapp.model.Articles
import com.example.plantapp.model.Plant
import com.example.plantapp.model.Species
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListArticlesViewModel : ViewModel() {
    private val mAuth: FirebaseAuth = Firebase.auth
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference()
    private val userfb: FirebaseUser =mAuth.currentUser!!
    private val uid:String=userfb.uid
    private var listArticles: MutableLiveData<List<Articles>> = MutableLiveData()
    private var listSpecies: MutableLiveData<List<Species>> = MutableLiveData()
    private var listPlant: MutableLiveData<List<Plant>> = MutableLiveData()
    private var listUser:MutableLiveData<List<User>> = MutableLiveData()

    fun getListArticles(): LiveData<List<Articles>> {
        return listArticles
    }
    fun getListSpecies(): LiveData<List<Species>> {
        return listSpecies
    }
    fun getUser(): LiveData<List<User>>{
        return listUser
    }
    fun getPlant():LiveData<List<Plant>>{
        return  listPlant
    }
    fun loadUser(){
        ref.child("User").child(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<User>()
                val user= snapshot.getValue(User::class.java)!!
                list.add(user)
                listUser.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun loadData() {
        ref.child("Articles").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Articles>()
                for (data: DataSnapshot in snapshot.children) {
                    val articles = data.getValue(Articles::class.java)
                    if (articles != null) {
                        list.add(articles)
                    }
                }
                listArticles.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun loadSpecies(){
        ref.child("Data").child("Species").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list1 = mutableListOf<Species>()
                var listpl= mutableListOf<Plant>()
                var listplt= mutableListOf<Plant>()
                for (data: DataSnapshot in snapshot.children) {
                    val species = data.getValue(Species::class.java)
                    if (species != null) {
                        if(species.listPlant!=null){
                            listpl=species.listPlant!!
                            listplt.addAll(listpl)
                        }
                        list1.add(species)
                    }
                }
                listSpecies.postValue(list1)
                listPlant.postValue(listplt)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}