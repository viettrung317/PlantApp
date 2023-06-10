package com.example.plantapp.model

data class User(var userName: String?="", var email: String?=null, var imagesource: String?=null,var listPhotoGraphy:MutableList<Plant>?=null,var listPlant:MutableList<Plant>?=null,var listArticlesSave:MutableList<Articles>?=null,var listArticlesLike:MutableList<Articles>?=null,var listMyArticles:MutableList<Articles>?=null,var stars: MutableMap<String, Boolean> = HashMap()):java.io.Serializable {


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userName" to userName,
            "email" to email,
            "imagesource" to imagesource,
            "listPhotoGraphy" to listPhotoGraphy,
            "listPlant" to listPlant,
            "listArticlesSave" to listArticlesSave,
            "listArticlesLike" to listArticlesLike,
            "listMyArticles" to listMyArticles,
            "stars" to stars
        )
    }

}