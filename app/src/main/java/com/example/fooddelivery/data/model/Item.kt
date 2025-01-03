package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id_item: String,
    val name: String,
    val image: String?,
    val price: Double? ,
    val id_restaurant: String? ,
    val Type: String ,
    val ingredient: String
)
