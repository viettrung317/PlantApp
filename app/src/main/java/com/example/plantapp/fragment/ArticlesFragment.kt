package com.example.plantapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.TypeAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentArticlesBinding
import com.example.plantapp.model.Articles
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ArticlesFragment : Fragment() {
    private lateinit var binding:FragmentArticlesBinding
    private val mAuth: FirebaseAuth = Firebase.auth
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference()
    private val userfb: FirebaseUser =mAuth.currentUser!!
    private val uid:String=userfb.uid
    private lateinit var user: User;
    private lateinit var userOwner: User;
    private var articles: Articles = Articles()
    private var positionArticles:Int=0;
    private var listLikeArticles= mutableListOf<Articles>()
    private var listTypeArticles= mutableListOf<String>()
    private var listFollow= mutableListOf<String>()
    private var listFollowing= mutableListOf<String>()
    private var likeArticles=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentArticlesBinding.inflate(layoutInflater,container,false)
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

    private fun getData(user: User) {
        var bundle=arguments
        articles=bundle?.getSerializable("articles") as Articles
        positionArticles=bundle.getInt("positionArticles")
        likeArticles=bundle.getBoolean("isLike")
        if(articles.imageArticles!=null){
            Picasso.get().load(articles.imageArticles).into(binding.imageArticles)
        }
        if(articles?.typeArticles!=null) {
            listTypeArticles = articles?.typeArticles!!
        }else{
            listTypeArticles= mutableListOf()
        }
        var linearLayoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        var adapter=TypeAdapter(listTypeArticles)
        binding.rcvTypeArticles.adapter=adapter
        if(articles.titleArticles!=null){
            binding.txtTitleArticles.text=articles.titleArticles
        }
        if(articles.avatarOwner!=null){
            Picasso.get().load(articles.avatarOwner).into(binding.imgAvatarArticles)
        }
        if(articles.nameOwner!=null){
            binding.txtNameOwnerArticles.text=articles.nameOwner
        }
        if(articles.time!=null){
            binding.txtTimeArticles.text=articles.time
        }
        if(articles.content!=null){
            binding.txtContentArticles.text=articles.content
        }
        if(likeArticles){
            binding.imageLikeArticles.setImageResource(R.drawable.ellipse3)
        }
        else{
            binding.imageLikeArticles.setImageResource(R.drawable.ellipse4)
        }
        getOwnerArticles(articles,user,likeArticles)
    }

    private fun getOwnerArticles(articles: Articles, user: User, likeArticles: Boolean) {
        var uidOwner=articles.uidOwner
        if (uidOwner != null) {
            ref.child("User").child(uidOwner).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userOwner= snapshot.getValue(User::class.java)!!
                    var isFollow=false
                    for(follow:String in userOwner.listFollow!!){
                        if(follow.equals(user.email)){
                            isFollow=true
                        }
                    }
                    if(isFollow){
                        binding.txtTimeArticles.text="- Unfollow"
                    }else{
                        binding.txtTimeArticles.text="+ Follow"
                    }
                    if(user.listArticlesLike!=null){
                        listLikeArticles= user.listArticlesLike!!
                    }
                    setEvents(user,userOwner,isFollow,articles,listLikeArticles,likeArticles)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun setEvents(
        user: User,
        userOwner: User,
        follow: Boolean,
        articles: Articles,
        listLikeArticles: MutableList<Articles>,
        likeArticles: Boolean
    ) {
        var uidOwner:String=""
        if(articles.uidOwner!=null){
            uidOwner=articles.uidOwner!!
        }
        binding.imageLikeArticles.setOnClickListener{
            if (likeArticles) {
                deletelike(user,listLikeArticles,articles,positionArticles,likeArticles)
            } else {
                addLike(user)
            }
        }

        binding.btnFollow.setOnClickListener{
            if(follow){
                listFollowing= user.listFollowing!!
                listFollowing.remove(uidOwner)
                user.listFollowing=listFollowing
                listFollow=userOwner.listFollow!!
                user.email?.let {listFollow.remove(it)}
                userOwner.listFollow=listFollow
                ref.child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                    if(it.isSuccessful){
                        ref.child(uidOwner).updateChildren(userOwner.toMap()).addOnCompleteListener{
                            binding.txtTimeArticles.text="- Unfollow"
                        }
                    }
                }
            }else{
                if(user.listFollowing!=null){
                    listFollowing= user.listFollowing!!
                }
                listFollowing.add(uidOwner)
                user.listFollowing=listFollowing
                if(userOwner.listFollow!=null){
                    listFollow=userOwner.listFollow!!
                }
                listFollow.add(user.email!!)
                userOwner.listFollow=listFollow
                ref.child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                    if(it.isSuccessful){
                        ref.child(uidOwner).updateChildren(userOwner.toMap()).addOnCompleteListener{
                            binding.txtTimeArticles.text="+ Follow"
                        }
                    }
                }
            }
        }

    }

    private fun addLike(user: User) {

    }

    private fun deletelike(
        user: User,
        listLikeArticles: MutableList<Articles>,
        articles: Articles,
        positionArticles: Int,
        likeArticles: Boolean
    ) {
        if(likeArticles){
            listLikeArticles.remove(articles)
            user.listArticlesLike=listLikeArticles
            ref.child("User").child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                if(it.isSuccessful){
                    var listLike= mutableListOf<String>()
                    if (articles.listLikeArticles != null) {
                        listLike = articles.listLikeArticles!!
                    }
                    user.email?.let { listLike.remove(it)}
                    articles.listLikeArticles=listLike
                    ref.child("Articles").child(positionArticles.toString()).updateChildren(articles.toMap()).addOnCompleteListener{
                        if(it.isSuccessful){
                            if(isAdded){
                                getUser()
                            }
                        }
                    }

                }
            }
        }else {
            var listLike = mutableListOf<String>()
            if (articles.listLikeArticles != null) {
                listLike = articles.listLikeArticles!!
            }
            user.email?.let { listLike.add(it) }
            articles.listLikeArticles = listLike
            ref.child("Articles").child(positionArticles.toString()).updateChildren(articles.toMap())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listLikeArticles.add(articles)
                        user.listArticlesLike = listLikeArticles
                        ref.child("User").child(uid).updateChildren(user.toMap())
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    if (isAdded) {
                                        getUser()
                                    }
                                }
                            }
                    }
                }
        }
    }
}