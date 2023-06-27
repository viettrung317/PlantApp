package com.example.plantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantapp.Adapter.ArticlesAdapter
import com.example.plantapp.Adapter.SelecterAdapter
import com.example.plantapp.R
import com.example.plantapp.databinding.FragmentArticlesProfileBinding
import com.example.plantapp.model.Articles
import com.example.plantapp.model.Selecter
import com.example.plantapp.model.User
import com.example.plantapp.viewModel.ViewModel

class ArticlesProfileFragment : Fragment(),ArticlesAdapter.OnItemClickListener {
    private lateinit var viewModel: ViewModel
    private lateinit var binding:FragmentArticlesProfileBinding
    private var listArticles= mutableListOf<Articles>()
    private var listArticlesUser= mutableListOf<Articles>()
    private lateinit var user: User;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentArticlesProfileBinding.inflate(layoutInflater,container,false)
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
            viewModel.getListArticles().observe(viewLifecycleOwner) { listArticles1 ->
                listArticles= listArticles1 as MutableList<Articles>
                setData(user,listArticles)
            }
            viewModel.loadData()
        }
        viewModel.loadUser()
    }

    private fun setData(user: User, listArticles: MutableList<Articles>) {
        getList(user)
    }

    private fun getList(user: User) {
        var selecterAdapter = SelecterAdapter(requireContext(), R.layout.item_selecterlayout, getlist())
        binding.spinner.setAdapter(selecterAdapter)
        binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (selecterAdapter.getItem(i)?.item.equals("Your articles")) {
                        if(user.listMyArticles!=null){
                            listArticlesUser=user.listMyArticles!!
                            getlistArticles(listArticlesUser)
                        }
                } else if (selecterAdapter.getItem(i)?.item
                        .equals("Articles liked")
                ) {
                    if(user.listArticlesLike!=null){
                        listArticlesUser=user.listArticlesLike!!
                        getlistArticles(listArticlesUser)
                    }
                } else if (selecterAdapter.getItem(i)?.item.equals("Articles saved")) {
                    if(user.listArticlesSave!=null){
                        listArticlesUser=user.listArticlesSave!!
                        getlistArticles(listArticlesUser)
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
    }

    private fun getlistArticles(listArticles: MutableList<Articles>) {
        val linearLayoutManager=LinearLayoutManager(requireContext())
        binding.rcvArticlesProfile.layoutManager=linearLayoutManager
        val adapter=ArticlesAdapter(this.user,listArticles,ListArticlesFragment())
        binding.rcvArticlesProfile.adapter=adapter
    }

    private fun getlist(): List<Selecter?> {
        var list= mutableListOf<Selecter>()
        list.add(Selecter("Your articles"))
        list.add(Selecter("Articles liked"))
        list.add(Selecter("Articles saved"))
        return list
    }

    override fun onItemClick(position: Int, like: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onClickLike(
        user: User,
        listArticles: MutableList<Articles>,
        articles: Articles,
        position: Int,
        like: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickSave(
        user: User,
        listArticles: MutableList<Articles>,
        articles: Articles,
        position: Int,
        save: Boolean
    ) {
        TODO("Not yet implemented")
    }
}