package com.example.plantapp.model

data class Plant(var plantName:String?="",var imagePlant:String?="",var type:String?="",var local:String="",var kingDom:String?="",var family: String?="",var description:String?="",var listLike:MutableList<String>?=null,var star: Float?=null,var stars: MutableMap<String, Boolean> = HashMap()):java.io.Serializable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "plantName" to plantName,
            "imagePlant" to imagePlant,
            "type" to type,
            "local" to local,
            "kingDom" to kingDom,
            "family" to family,
            "description" to description,
            "listLike" to listLike,
            "star" to star,
            "stars" to stars
        )
    }
}