package com.example.plantapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantapp.model.Articles
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

    fun getListArticles(): LiveData<List<Articles>> {
        return listArticles
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
}