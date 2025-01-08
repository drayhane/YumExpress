package com.example.fooddelivery.data.model

import kotlinx.serialization.Serializable



@Serializable
data class User1(
    val id_user: String,
    val email: String,
    val password: String,
    val profile_picture: String?,
    val num_tel: String,
    val adress: String,
    val location: String?,
    val name: String,
    val id_card: String?
)

@Serializable
data class Cart(
    val id_card: String,
    val total_price: Double,
    val food_note: String,
    val Id_rest : String?,
    val is_active : Boolean
)