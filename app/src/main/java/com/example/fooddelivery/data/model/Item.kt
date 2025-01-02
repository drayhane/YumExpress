package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class item(
    val id_item: String,
    val name: String? = null,
    val image: String? = null,
    val price: Double ,
    val id_restaurant: String,
    val Type: String? = null,
    val ingredient: String? = null
)
