package com.example.plantapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantapp.R
import com.example.plantapp.fragment.ListArticlesFragment
import com.example.plantapp.fragment.ListPlantFragment
import com.example.plantapp.model.Articles
import com.example.plantapp.model.Plant
import com.example.plantapp.model.User
import com.squareup.picasso.Picasso

class ArticlesAdapter(private val user: User,private val listArticles: MutableList<Articles>, private val listener: ListArticlesFragment): RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {
    private var listSave= mutableListOf<Articles>()
    private var listLike= mutableListOf<Articles>()
    inner class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArticlesItem = itemView.findViewById<ImageView>(R.id.imgArticlesItem)
        val imgAvatarItem = itemView.findViewById<ImageView>(R.id.imgAvatarItem)
        val txtTitleItem = itemView.findViewById<TextView>(R.id.txtTitleItem)
        val txtNameOwnerItem = itemView.findViewById<TextView>(R.id.txtNameOwnerItem)
        val txtTimeArticlesItem = itemView.findViewById<TextView>(R.id.txtTimeArticlesItem)
        val imgSaveArticlesItem = itemView.findViewById<ImageView>(R.id.imgSaveArticlesItem)
        val imgLikeArticlesItem = itemView.findViewById<ImageView>(R.id.imgLikeArticlesItem)
        init {
            imgArticlesItem.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }


        }

    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onClickLike(user: User, listArticles: MutableList<Articles>,articles: Articles, position: Int, like:Boolean)
        fun onClickSave(user: User, listArticles: MutableList<Articles>,articles: Articles, position: Int, save:Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articles_layout, parent, false)
        return ArticlesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val item=listArticles[position]
        Picasso.get().load(item.imageArticles).into(holder.imgArticlesItem)
        holder.txtTitleItem.text = item.titleArticles
        holder.txtNameOwnerItem.text=item.nameOwner
        holder.txtTimeArticlesItem.text=item.time
        Picasso.get().load(item.avatarOwner).into(holder.imgAvatarItem)
        var haveArticlesSave=false
        var haveArticlesLike=false
        if(user.listArticlesSave!=null){
            listSave= user.listArticlesSave!!
        }
        if(user.listArticlesLike!=null){
            listLike= user.listArticlesLike!!
        }
        for(articles:Articles in listSave){
            if(articles.titleArticles.equals(item.titleArticles)){
                haveArticlesSave=true
            }
        }
        if(haveArticlesSave){
            holder.imgSaveArticlesItem.setImageResource(R.drawable.save_1)
        }else{
            holder.imgSaveArticlesItem.setImageResource(R.drawable.save)
        }
        for(articles:Articles in listLike){
            if(articles.titleArticles.equals(item.titleArticles)){
                haveArticlesLike=true
            }
        }
        if(haveArticlesLike){
            holder.imgLikeArticlesItem.setImageResource(R.drawable.heart_1)
        }else{
            holder.imgLikeArticlesItem.setImageResource(R.drawable.heart_0)
        }
        holder.imgLikeArticlesItem.setOnClickListener{
            if (position != RecyclerView.NO_POSITION) {
                if(haveArticlesLike){
                    holder.imgLikeArticlesItem.setImageResource(R.drawable.heart_1)
                    listener.onClickLike(user,listLike,item,position,haveArticlesLike)
                    haveArticlesLike=false
                }else{
                    holder.imgLikeArticlesItem.setImageResource(R.drawable.heart_0)
                    listener.onClickLike(user,listLike,item,position,haveArticlesLike)
                    haveArticlesLike=true
                }
            }
        }
        holder.imgSaveArticlesItem.setOnClickListener{
            if (position != RecyclerView.NO_POSITION) {
                if(haveArticlesSave){
                    holder.imgSaveArticlesItem.setImageResource(R.drawable.save)
                    listener.onClickSave(user,listSave,item,position,haveArticlesSave)
                    haveArticlesSave=false
                }else{
                    holder.imgSaveArticlesItem.setImageResource(R.drawable.save_1)
                    listener.onClickSave(user,listSave,item,position,haveArticlesSave)
                    haveArticlesSave=true
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return listArticles.size
    }
}