package com.example.plantapp.model

data class Species(var nameSpecies:String?="",var listPlant: MutableList<Plant>?=null,var stars: MutableMap<String, Boolean> = HashMap()):java.io.Serializable{
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nameSpecies" to nameSpecies,
            "listPlant" to listPlant,
            "stars" to stars
        )
    }
}
