package com.example.plantapp.model

data class Articles(var uidOwner:String?="",var nameOwner:String?="",var avatarOwner:String?="",var titleArticles:String?="",var imageArticles:String?="",var typeArticles:MutableList<String>?=null,var content:String="",var time:String?="",var listLikeArticles:MutableList<String>?=null,var stars: MutableMap<String, Boolean> = HashMap()):java.io.Serializable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uidOwner" to uidOwner,
            "nameOwner" to nameOwner,
            "avatarOwner" to avatarOwner,
            "titleArticles" to titleArticles,
            "imageArticles" to imageArticles,
            "typeArticles" to typeArticles,
            "content" to content,
            "time" to time,
            "listLikeArticles" to listLikeArticles,
            "stars" to stars
        )
    }
}