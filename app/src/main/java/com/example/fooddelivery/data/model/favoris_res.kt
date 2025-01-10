package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable

@Serializable

data class favoris_res (
    val id_user:String,
    val id_restaurant:String
)

