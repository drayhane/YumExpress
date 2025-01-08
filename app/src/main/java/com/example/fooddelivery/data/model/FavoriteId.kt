package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteId(
    val id_restaurant: String = ""
)
