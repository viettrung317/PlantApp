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
import com.example.plantapp.Adapter.ArticlesAdapter
import com.example.plantapp.databinding.FragmentListArticlesBinding
import com.example.plantapp.model.Articles
import com.example.plantapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ListArticlesFragment : Fragment(),ArticlesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentListArticlesBinding
    private val mAuth: FirebaseAuth = Firebase.auth
    private val data: FirebaseDatabase = Firebase.database
    private val ref: DatabaseReference =data.getReference()
    private val userfb: FirebaseUser =mAuth.currentUser!!
    private val uid:String=userfb.uid
    private var listArticles= mutableListOf<Articles>()
    private var articles:Articles= Articles()
    private lateinit var user: User;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentListArticlesBinding.inflate(layoutInflater,container,false)
        getData()
        return binding.root
    }
    private fun setEvent(user: User, listArticles: MutableList<Articles>) {
        binding.imgExitArticles.setOnClickListener{
            if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                // FragmentManager không có Fragment nào trên Backstack, không gọi onBackPressed để tránh đóng Activity.
                // Thực hiện hành động khác nếu cần thiết.
            }
        }
        binding.txtSeachArticles.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imageView113.visibility=View.GONE
                var list= mutableListOf<Articles>()
                for (articles: Articles in listArticles) {
                    if (articles.titleArticles?.toLowerCase(Locale.getDefault())
                            ?.contains(s.toString().toLowerCase(Locale.getDefault())) == true)

                    {
                        list.add(articles)
                    }
                }
                val adapter = ArticlesAdapter(user,list,this@ListArticlesFragment)
                binding.rcvArticles.adapter = adapter

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    private fun getData(){
        val bundle=arguments
        user= bundle?.getSerializable("user") as User
        ref.child("Articles").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data:DataSnapshot in snapshot.children){
                    articles=data.getValue(Articles::class.java)!!
                    listArticles.add(articles)
                }
                setData(user,listArticles)
                setEvent(user,listArticles)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setData(user: User, listArticles: MutableList<Articles>) {
        binding.rcvArticles.layoutManager=LinearLayoutManager(requireContext())
        val adapter=ArticlesAdapter(this.user,listArticles,this@ListArticlesFragment)
        binding.rcvArticles.adapter=adapter
        adapter.notifyDataSetChanged()
    }


    override fun onItemClick(position: Int) {
        val articles = listArticles[position]
        val bundle = Bundle()
        bundle.putSerializable("articles",articles)
        bundle.putInt("positionArticles",position)
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

    override fun onClickLike(user: User, listArticles: MutableList<Articles>, articles: Articles, position: Int, like: Boolean) {
        if(like){
            listArticles.remove(articles)
            user.listArticlesLike=listArticles
            ref.child("User").child(uid).updateChildren(user.toMap()).addOnCompleteListener{
                if(it.isSuccessful){
                    var listLike= mutableListOf<String>()
                    if (articles.listLikeArticles != null) {
                        listLike = articles.listLikeArticles!!
                    }
                    user.email?.let { listLike.remove(it) }
                    articles.listLikeArticles=listLike
                    ref.child("Articles").child(position.toString()).updateChildren(articles.toMap())

                }
            }
        }else{
            var listLike= mutableListOf<String>()
            if(articles.listLikeArticles!=null){
                listLike=articles.listLikeArticles!!
            }
            user.email?.let { listLike.add(it) }
            articles.listLikeArticles=listLike
            ref.child("Articles").child(position.toString()).updateChildren(articles.toMap()).addOnCompleteListener {
                if(it.isSuccessful){
                    listArticles.add(articles)
                    user.listArticlesLike=listArticles
                    ref.child("User").child(uid).updateChildren(user.toMap())
                }
            }

        }
    }

    override fun onClickSave(user: User, listArticles: MutableList<Articles>, articles: Articles, position: Int, save: Boolean) {
        if(save){
            listArticles.remove(articles)
            user.listArticlesSave=listArticles
            ref.child("User").child(uid).updateChildren(user.toMap())
        }else{
            listArticles.add(articles)
            user.listArticlesSave=listArticles
            ref.child("User").child(uid).updateChildren(user.toMap())
        }
    }

}