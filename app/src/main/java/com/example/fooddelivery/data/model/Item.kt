package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id_item: String,
    val name: String,
    val image: Int,
    val price: Int,
    val id_restaurant: String,
    val Type: String,
    val ingredient: String
)