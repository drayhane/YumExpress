package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class item(
    val id_item: String,
    val name: String? = null,
    val image: String? = null,
    val price: Double? = null,
    val id_restaurant: String? = null,
    val Type: String? = null,
    val ingredient: String? = null
)
